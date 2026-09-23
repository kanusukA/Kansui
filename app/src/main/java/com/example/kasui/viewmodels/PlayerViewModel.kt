package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class PlayerViewModel(
    private val mainViewModel: MainViewModel
) : ViewModel() {

    val trackQueue = mainViewModel.dbTracks.combine(PlayerListener.trackQueue) { tracks, ids ->
        ids.mapNotNull { id -> tracks.find { it.id.toString() == id.mediaId } }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )


    val currentTrack = PlayerListener.currentTrackId.map { id ->
        val track = trackQueue.value.firstOrNull { it.id.toString() == id }
        track
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val currentAlbum = currentTrack.map { track ->
        mainViewModel.dbAlbums.value.find { it.album.id == track?.albumId }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

}