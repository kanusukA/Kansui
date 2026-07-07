package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.request.MediaManagerState
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.NavState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TopBarViewModel : ViewModel() {
    // EXTERNAL
    lateinit var navState: StateFlow<NavRoutes>

    private var _topBarSelectionState: MutableStateFlow<TopBarSelectionState> =
        MutableStateFlow(TopBarSelectionState.NONE)
    val topBarSelectionState = _topBarSelectionState.asStateFlow()

    fun setTopBarSelectionState(state: TopBarSelectionState) {
        _topBarSelectionState.update { state }
    }

    val selectedAlbum = NavManager.selectedAlbum
    val username = LastFmManager.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        ""
    )

    val mediaState = MediaManager.mediaState

    // INTERNAL

    init {
        viewModelScope.launch {
            navState = NavManager.navStates
        }
    }
}

// THESE ARE ALL THE COMBINED POSITIONS THE TOP BAR STATE CAN HAVE
enum class TopBarSelectionState {
    NONE,
    ALBUM_WELCOME,
    TRACKS_WELCOME
}

sealed class TopSelectionBars(val entries: Map<String, TopBarSelectionState>) {
    data class WelcomeSelectionBar(
        val states: Map<String, TopBarSelectionState> = mapOf(
            "Albums" to TopBarSelectionState.ALBUM_WELCOME,
            "Tracks" to TopBarSelectionState.TRACKS_WELCOME
        )
    ) :
        TopSelectionBars(states)
}