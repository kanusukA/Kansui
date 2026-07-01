package com.example.kasui.Presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LastFmViewModel : ViewModel() {

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

    val username: StateFlow<String> = LastFmManager.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    fun initMain() {

    }


    fun albumSyncSelection(selectedAlbums: List<Album>) {
        viewModelScope.launch(Dispatchers.IO) {
            LastFmManager.fetchAlbumResults("I Let it in and it took everything")
        }

    }

}