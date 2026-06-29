package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Presentation.NavManager
import kotlinx.coroutines.flow.map

class HomeViewModel : ViewModel() {
    val songList = MediaManager.rawSongList
    val albumsList = MediaManager.rawAlbumList

    val albumResult = MediaManager.searchAlbumList

    fun navToAlbumScreen(album: Album) {
        NavManager.navToAlbumView(album)
    }


}