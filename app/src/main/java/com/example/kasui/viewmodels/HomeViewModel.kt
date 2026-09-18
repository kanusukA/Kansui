package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Presentation.NavManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val albumRepository: AlbumRepository,
    private val mainViewModel: MainViewModel
) : ViewModel() {

    val songList = MediaManager.rawSongList
    val albumsList = mainViewModel.rawAlbums

    val albumResult = MediaManager.searchAlbumList


    fun navToAlbumScreen(album: Album) {
        NavManager.navToAlbumView(album)
    }


}