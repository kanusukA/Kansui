package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.LastFm.LastFmSearchAlbum
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WelcomeViewmodel : ViewModel() {

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

    val rawAlbums = MediaManager.rawAlbumList

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

    fun albumSyncLastFm() {
        NavManager.changeNavState(NavRoutes.WelcomeSearchAlbum())
        viewModelScope.launch(Dispatchers.IO) {
            val searchMap = mutableMapOf<Int, LastFmSearchAlbum>()
            println("selectedAlbum Size : ${selectedAlbumsList.value.size}")
            selectedAlbumsList.value.forEach { index ->
                val album = rawAlbums.value[index]
                val result =
                    LastFmManager.fetchAlbumResults(album.albumAttributes.albumName, limit = 3)
                if (result != null) {
                    searchMap[index] = result
                    _searchAlbums.update { searchMap.toMap() }
                }

            }
        }

    }

}