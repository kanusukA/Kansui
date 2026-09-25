package com.example.kasui.Presentation

import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Presentation.screens.home.AlbumScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

enum class NavState {
    HOME,
    SEARCH,
    LIBRARY,

    ALBUM
}

sealed class NavRoutes(
    var popBack: Boolean = false,
    val navRoute: String,
    val welcomeSubRoutes: WelcomeSubRoutes = WelcomeSubRoutes.NONE
) {

    companion object {
        val all = listOf(Home(), Search(), Library(), Album(), PlayerView())
    }

    data class Home(val route: String = "Home", var popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Search(val route: String = "Search", var popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Library(val route: String = "Library", var popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class Album(val route: String = "Album", var popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class PlayerView(val route: String = "Player", var popBackStack: Boolean = false) :
        NavRoutes(popBackStack, navRoute = route)

    data class WelcomeLogin(
        val route: String = "Welcome",
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.LOGIN,
        var popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route, welcomeSubRoutes = subRoute)

    data class WelcomeSetupAlbum(
        val route: String = "Welcome",
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.SETUP,
        var popBackStack: Boolean = false,
    ) :
        NavRoutes(popBackStack, navRoute = route, welcomeSubRoutes = subRoute)

    data class WelcomeSearchAlbum(
        val route: String = "Welcome",
        val subRoute: WelcomeSubRoutes = WelcomeSubRoutes.SEARCH,
        var popBackStack: Boolean = false,
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

    private var _playerViewState: MutableStateFlow<PlayerFullViewState> =
        MutableStateFlow(PlayerFullViewState.HIDDEN)
    val playerViewState = _playerViewState.asStateFlow()


    private var _selectedAlbum: MutableStateFlow<Album?> = MutableStateFlow(null)
    val selectedAlbum = _selectedAlbum.asStateFlow()

    fun changeNavState(state: NavRoutes) {
        if (state == NavRoutes.Album() && selectedAlbum.value == null) {
            throw Exception("ALBUM VIEW SELECTED WITHOUT A ALBUM TO SHOW!")
        }
        _navStates.update { state }
    }

    fun goBack(previousRoute: String) {
        NavRoutes.all.forEach {
            if (it.navRoute == previousRoute) {
                changeNavState(it.apply { popBack = true })
                return@forEach
            }
        }
    }

    fun navToAlbumView(album: Album) {
        _selectedAlbum.update { album }
        changeNavState(NavRoutes.Album())
    }

    private var _miniPlayerViewVisible = MutableStateFlow<Boolean>(true)
    val miniPlayerVisible = _miniPlayerViewVisible

//    private var _playingTrack: MutableStateFlow<Track?> = MutableStateFlow(
//        Track(
//            id = 0L,
//            albumName = "Long Nights and Wasted Affairs",
//            artistName = "Mind's Eye",
//            durationInMillis = 3000,
//            name = "astrology Girl",
//            hasLyrics = false,
//            uri = Uri.EMPTY
//        )
//    )
//    val playingTrack = _playingTrack.asStateFlow()

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

    fun setMiniPlayerView(visibility: Boolean) {
        _miniPlayerViewVisible.update { visibility }
    }

    fun changePlayerViewState(state: PlayerFullViewState) {
        _playerViewState.update { state }
    }

//    fun playTrack(track: Track) {
//        _playingTrack.update { track }
//    }

}

enum class PlayerFullViewState {
    PLAYING,
    QUEUE,
    LYRICS,
    HIDDEN
}

