package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.request.MediaManagerState
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.NavState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TopBarViewModel : ViewModel() {
    // EXTERNAL
    lateinit var navState: StateFlow<NavRoutes>

    val selectedAlbum = NavManager.selectedAlbum

    val mediaState = MediaManager.mediaState

    // INTERNAL

    init {
        viewModelScope.launch {
            navState = NavManager.navStates
        }
    }
}