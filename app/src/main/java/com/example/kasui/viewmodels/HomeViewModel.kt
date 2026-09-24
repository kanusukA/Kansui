package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Presentation.NavManager

class HomeViewModel(
    private val albumRepository: AlbumRepository,
    private val mainViewModel: MainViewModel
) : ViewModel() {
    val albumsList = mainViewModel.dbAlbums
    fun navToAlbumScreen(album: Album) {
        NavManager.navToAlbumView(album)
    }


}