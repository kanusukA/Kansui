package com.example.kasui.Presentation

import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Presentation.screens.home.AlbumScreenState
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
    val navRoute: String,
    val welcomeSubRoutes: WelcomeSubRoutes = WelcomeSubRoutes.NONE
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
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.LOGIN,
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route, welcomeSubRoutes = subRoute)

    data class WelcomeSetupAlbum(
        val route: String = "Welcome",
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.SETUP,
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route, welcomeSubRoutes = subRoute)

    data class WelcomeSearchAlbum(
        val route: String = "Welcome",
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.SEARCH,
        val popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route, welcomeSubRoutes = subRoute)
}


enum class WelcomeSubRoutes {
    LOGIN,
    SETUP,
    SEARCH,
    NONE
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

    // INTER_VIEWMODEL_VARIABLES
    private var _albumScreenState: MutableStateFlow<AlbumScreenState> =
        MutableStateFlow(AlbumScreenState.DEFAULT)
    val albumScreenState = _albumScreenState.asStateFlow()

    private var _topBarVisibility = MutableStateFlow(true)
    val topBarVisibility = _topBarVisibility.asStateFlow()

    fun setAlbumScreenState(albumScreenState: AlbumScreenState) {
        _albumScreenState.update { albumScreenState }
    }

    fun setTopBarVisibility(visibility: Boolean) {
        _topBarVisibility.update { visibility }
    }

}

