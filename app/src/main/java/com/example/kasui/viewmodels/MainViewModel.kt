package com.example.kasui.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.local.AlbumDatabase
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Track
//import com.example.kasui.Data.structure.MusicBrainz.Media
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.TagLib
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val lastfmState = LastFmManager.lastFmState

    var tagged: Boolean = false

    // VIEW MODELS
    var homeViewModel: HomeViewModel

    val username: StateFlow<String> = LastFmManager.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val mediaManagerState = MediaManager.mediaState
    var dbAlbums: StateFlow<List<Album>>
    var dbTracks: StateFlow<List<Track>>

    val loginCoroutine = CoroutineScope(Dispatchers.IO)

    init {
        val albumDao = AlbumDatabase.getInstance(application.applicationContext).getAlbumDao()
        val albumRepo = AlbumRepository(albumDao)

        dbAlbums = albumRepo.albums.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

        dbTracks = albumRepo.getAllTracks()
            .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())



        homeViewModel = HomeViewModel(albumRepo, this)

    }

    fun initMain(context: Context) {
        Player.initPlayer(context)
        MediaManager.initRepo(context)

        syncMusicFiles()
    }

    fun syncMusicFiles() {
        viewModelScope.launch {
            MediaManager.fetchMusicFiles(application.applicationContext)
            MediaManager.saveAlbums(
                MediaManager.insertAlbums.value
            )
        }
    }

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

//    fun loadArtwork(artwork: Artwork){
//        if (artwork.bitmap == null && artwork.uri != null){
//            artwork.bitmap = fetchArtworkFromTrackUri(artwork.uri!!)
//        }
//        return artwork.bitmap
//    }


}




