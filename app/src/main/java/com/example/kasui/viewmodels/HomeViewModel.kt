package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Presentation.NavManager
import com.example.kasui.TagLib
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val albumRepository: AlbumRepository,
    private val mainViewModel: MainViewModel
) : ViewModel() {
    val albumsList = mainViewModel.dbAlbums


    fun navToAlbumScreen(album: Album) {
        NavManager.navToAlbumView(album)
    }


}