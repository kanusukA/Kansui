package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.LastFm.LastFmSearchAlbum
import com.example.kasui.Data.LastFm.LastFmSearchTrack
import com.example.kasui.Data.LastFm.LastFmTrack
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


class WelcomeViewmodel : ViewModel() {

    private var _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    fun setUsername(str: String) {
        _username.update { str }
    }

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setPassword(str: String) {
        _password.update { str }
    }

    private var _selectedAlbumsList = MutableStateFlow<List<Int>>(emptyList())
    val selectedAlbumsList = _selectedAlbumsList.asStateFlow()

    fun setSelectedAlbumList(selAlbum: List<Int>) {
        _selectedAlbumsList.update { selAlbum }
    }

    private var _selectedSongAlbumList = MutableStateFlow<List<Int>>(emptyList())
    val selectedSongAlbumList = _selectedSongAlbumList.asStateFlow()

    fun setSelectedSongAlbumList(selSongAlbum: List<Int>) {
        _selectedSongAlbumList.update { selSongAlbum }
    }

    // HERE INT IS THE RAW ALBUM INDEX
    private var _searchAlbums = MutableStateFlow<Map<Int, LastFmSearchAlbum>>(emptyMap())
    val searchAlbum = _searchAlbums.asStateFlow()

    // Int - SearchAlbum Key / i.e. rawAlbum index , Int - LastFmSearchAlbum Index for that value
    private var _selectedSearchAlbums = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedSearchAlbums = _selectedSearchAlbums.asStateFlow()

    fun setSelectedSearchAlbums(key: Int, value: Int) {
        _selectedSearchAlbums.update {
            _selectedSearchAlbums.value.toMutableMap().apply { put(key, value) }.toMap()
        }
    }

    // Int - rawSong Index / Track Search result
    private var _searchSongAlbums = MutableStateFlow<Map<Int, List<LastFmTrack>>>(emptyMap())
    val searchTrackAlbums = _searchSongAlbums.asStateFlow()

    // Int - searchSong Key / LastFmSearchTrack index
    private var _selectedSongSearchTracks = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val selectedSongSearchTracks = _selectedSongSearchTracks.asStateFlow()

    fun setSelectedSearchTrack(key: Int, value: Int) {
        _selectedSongSearchTracks.update {
            _selectedSongSearchTracks.value.toMutableMap().apply { put(key, value) }.toMap()
        }
    }


    val rawAlbums = MediaManager.rawAlbumList
    val rawSongs = MediaManager.rawSongList

    val lastfmState = LastFmManager.lastFmState.map {
        if (it == LASTFM_STATE.SIGNED_IN && NavManager.navStates.value == NavRoutes.WelcomeLogin()) {
            NavManager.changeNavState(NavRoutes.WelcomeSetupAlbum())
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        LASTFM_STATE.SIGNED_OUT
    )


    fun loginLastFm(mainViewModel: MainViewModel) {
        mainViewModel.loginLastFm(username.value, password.value)
    }

    fun albumSyncLastFm() {
        NavManager.changeNavState(NavRoutes.WelcomeSearchAlbum())
        _searchAlbums.update { emptyMap() }
        _selectedSearchAlbums.update { emptyMap() }
        viewModelScope.launch(Dispatchers.IO) {
            val searchMap = mutableMapOf<Int, LastFmSearchAlbum>()
            val searchSongMap = mutableMapOf<Int, List<LastFmTrack>>()
            val selectedSearchMap =
                mutableMapOf<Int, Int>() // Used to prefill the selection Map with 0 index to select first result
            val selectedSearchSongMap = mutableMapOf<Int, Int>()
            for (index in selectedAlbumsList.value) {
                delay(500.milliseconds)
                val album = rawAlbums.value[index]
                if (selectedSongAlbumList.value.contains(index)) { // If the Album is selected as Song Albums
                    if (!album.albumRelationships?.tracks.isNullOrEmpty()) {
                        for (trackId in album.albumRelationships.tracks) {
                            for (rawSongIndex in rawSongs.value.indices) { // tracks are fetched from the id to rawSong
                                if (trackId == rawSongs.value[rawSongIndex].id) {
                                    val track =
                                        rawSongs.value.firstOrNull { it.id == trackId }
                                    if (track != null && track.attributes.albumName != null) {
                                        val result = LastFmManager.fetchTrackResults(
                                            track.attributes.albumName,
                                            track.attributes.artistName
                                        )
                                        if (result != null) {
                                            searchSongMap[rawSongIndex] = result
                                            selectedSearchSongMap[rawSongIndex] = 0
                                            _searchSongAlbums.update { searchSongMap }
                                            _selectedSongSearchTracks.update { selectedSearchSongMap }
                                        }
                                        delay(400.milliseconds)
                                        break
                                    }
                                }
                            }
                        }
                    }

                } else {


                    val result =
                        LastFmManager.fetchAlbumResults(
                            album.albumAttributes.albumName,
                            artist = album.albumAttributes.artistName,
                            limit = 3
                        )

                    if (result != null) {
                        selectedSearchMap[index] = 0
                        searchMap[index] = result
                        _selectedSearchAlbums.update { selectedSearchMap }
                        _searchAlbums.update { searchMap.toMap() }


                    }

                }


            }


        }

    }

}