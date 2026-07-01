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

sealed class NavRoutes(
    val popBack: Boolean = false,
    val navRoute: String
) {
    data class Home(val route: String = "Home", val popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Search(val route: String = "Search", val popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Library(val route: String = "Library", val popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Album(val route: String = "Album", val popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class WelcomeLogin(
        val route: String = "Welcome",
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route)

    data class WelcomeSetupAlbum(
        val route: String = "Welcome",
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route)

    data class WelcomeSearchAlbum(
        val route: String = "Welcome",
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route)
}

enum class WelcomeNavStage {
    LOGIN,
    SETUP
}

object NavManager {
    private var _navStates: MutableStateFlow<NavRoutes> = MutableStateFlow(NavRoutes.WelcomeLogin())
    val navStates: StateFlow<NavRoutes> = _navStates


    private var _selectedAlbum: MutableStateFlow<Album?> = MutableStateFlow(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    fun changeNavState(state: NavRoutes) {
        if (state == NavRoutes.Album() && selectedAlbum.value == null) {
            throw Exception("ALBUM VIEW SELECTED WITHOUT A ALBUM TO SHOW!")
        }
        _navStates.update { state }
    }

    fun navToAlbumView(album: Album) {
        _selectedAlbum.update { album }
        changeNavState(NavRoutes.Album())
    }

}

