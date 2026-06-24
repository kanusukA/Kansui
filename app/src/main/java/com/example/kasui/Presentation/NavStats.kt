package com.example.kasui.Presentation

import com.example.kasui.Data.structure.album.Album
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class NavState {
    HOME,
    SEARCH,
    LIBRARY,

    ALBUM
}

object NavManager {
    private var _navStates: MutableStateFlow<NavState> = MutableStateFlow(NavState.HOME)
    val navStates: StateFlow<NavState> = _navStates

    val selectedAlbum: MutableStateFlow<Album?> = MutableStateFlow(null)
    var _selectedAlbum = selectedAlbum.asStateFlow()

    fun changeNavState(state: NavState) {
        if (state == NavState.ALBUM && selectedAlbum.value == null) {
            throw Exception("ALBUM VIEW SELECTED WITHOUT A ALBUM TO SHOW!")
        }
        _navStates.update { state }
    }

    fun navToAlbumView(album: Album) {
        selectedAlbum.update { album }
        changeNavState(NavState.ALBUM)
    }

}