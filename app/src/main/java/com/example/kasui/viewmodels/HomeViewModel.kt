package com.example.kasui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.song.Song
import kotlinx.coroutines.flow.map

class HomeViewModel : ViewModel() {
    val songList = MediaManager.rawSongList

    val albumsList = MediaManager.rawAlbumList


}