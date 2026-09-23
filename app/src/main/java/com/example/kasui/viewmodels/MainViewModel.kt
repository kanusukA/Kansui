package com.example.kasui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.local.AlbumDatabase
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Track
//import com.example.kasui.Data.structure.MusicBrainz.Media
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val lastfmState = LastFmManager.lastFmState

    // VIEW MODELS
    var homeViewModel: HomeViewModel
//    var playerViewModel: PlayerViewModel
//    var topBarViewModel: TopBarViewModel


    val username: StateFlow<String> = LastFmManager.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val mediaManagerState = MediaManager.mediaState
    var dbAlbums: StateFlow<List<Album>>
    var dbTracks: StateFlow<List<Track>>

    val rawSongs = MediaManager.rawSongList

    val loginCoroutine = CoroutineScope(Dispatchers.IO)

    init {
        val albumDao = AlbumDatabase.getInstance(application.applicationContext).getAlbumDao()
        val albumRepo = AlbumRepository(albumDao)

        dbAlbums = albumRepo.albums.map {
            it.map { album ->
                if (album.album.artwork.bitmap == null && album.album.artwork.uri != null) {
                    album.album.artwork.bitmap = MediaManager.fetchArtworkFromTrackUri(
                        application.applicationContext,
                        album.album.artwork.uri!!
                    )
                }
                album
            }
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        dbTracks = albumRepo.getAllTracks()
            .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

        homeViewModel = HomeViewModel(albumRepo, this)
//        playerViewModel = PlayerViewModel(this)
//        topBarViewModel = TopBarViewModel(playerViewModel)


    }

    fun initMain(context: Context) {
        Player.initPlayer(context)
        MediaManager.initRepo(context)

//        viewModelScope.launch(Dispatchers.IO) {
//            LastFmManager.loadSessionKey(application.applicationContext)
//        }

        viewModelScope.launch(Dispatchers.IO) {
            MediaManager.fetchMusicFiles(application.applicationContext)
        }
    }

    // CREATE AND MANAGE VIEWMODEL BY YOURSELF!!!!!

    fun loginLastFm(username: String, password: String) {

        if (lastfmState.value == LASTFM_STATE.SIGNED_IN) {
            println("ALREADY LOGGED IN")
            when (NavManager.navStates.value) {
                //is NavRoutes.WelcomeLogin -> NavManager.changeNavState(NavRoutes.WelcomeSetupAlbum())
                is NavRoutes.WelcomeLogin -> NavManager.changeNavState(NavRoutes.Home())
                else -> {}
            }
            return
        }
        if (lastfmState.value == LASTFM_STATE.LOGGING_IN) {
            println("ALREADY ATTEMPTING A LOGIN")
            return
        }

        loginCoroutine.launch {
            val result = LastFmManager.initLastFm(
                context = application.applicationContext,
                username,
                password
            )
            if (result) {
                NavManager.changeNavState(NavRoutes.Home())
            }
        }

    }


}




