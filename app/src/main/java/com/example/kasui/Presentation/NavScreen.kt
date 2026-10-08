package com.example.kasui.Presentation

import android.content.ComponentName
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kasui.Presentation.NavRoutes.*
import com.example.kasui.Presentation.components.bottomBar.BottomBar
import com.example.kasui.Presentation.components.topBar.TopBar
import com.example.kasui.Presentation.screens.home.AlbumScreen
import com.example.kasui.Presentation.screens.home.HomeScreen
import com.example.kasui.Presentation.screens.home.PlayerFullView
import com.example.kasui.Presentation.screens.home.PlayerView
import com.example.kasui.Presentation.screens.home.WelcomeScreen
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.surfaceColor
import com.example.kasui.viewmodels.MainViewModel
import com.example.kasui.viewmodels.Player
import com.example.kasui.viewmodels.PlayerViewModel
import com.example.kasui.viewmodels.TopBarViewModel
import com.example.kasui.viewmodels.WelcomeViewmodel
import com.google.common.util.concurrent.MoreExecutors

@RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
@Composable
fun NavScreen(
    mainViewModel: MainViewModel
) {

    val context = LocalContext.current

    val playerViewModel: PlayerViewModel = viewModel {
        PlayerViewModel(mainViewModel)
    }

    // player


    DisposableEffect(Unit) {
        val sessionToken = SessionToken(context, ComponentName(context, Player::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener(
            {
                playerViewModel.playerController = controllerFuture.get()
            },
            MoreExecutors.directExecutor()
        )

        onDispose {
            MediaController.releaseFuture(controllerFuture)
        }

    }

    val navController = rememberNavController()


    val topBarViewModel: TopBarViewModel = viewModel() {
        TopBarViewModel(playerViewModel)
    }

    val navState by NavManager.navStates.collectAsStateWithLifecycle()

    val selectedAlbum by NavManager.selectedAlbum.collectAsStateWithLifecycle()

    var isScrolledPastFirstItem by remember {
        mutableStateOf(false)
    }

    val playerFullViewState by NavManager.playerViewState.collectAsStateWithLifecycle()

    val miniPlayerVisible by NavManager.miniPlayerVisible.collectAsStateWithLifecycle()

    val currentTrack by playerViewModel.currentTrack.collectAsStateWithLifecycle()

    val bottomBarVisible by NavManager.bottomBarVisible.collectAsStateWithLifecycle()

    // hide status bar
    val view = LocalView.current
    LaunchedEffect(playerFullViewState, navState) {
        if (playerFullViewState == PlayerFullViewState.LYRICS && navState.navRoute == NavRoutes.PlayerView().route) {
            val window = (view.context as ComponentActivity).window

            // 3. Initialize the Insets Controller
            val insetsController = WindowCompat.getInsetsController(window, view)

            // 4. Hide the status bar (notification bar)
            insetsController.hide(WindowInsetsCompat.Type.statusBars())

            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            val window = (view.context as ComponentActivity).window

            // 3. Initialize the Insets Controller
            val insetsController = WindowCompat.getInsetsController(window, view)

            // 4. Hide the status bar (notification bar)
            insetsController.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    LaunchedEffect(navState, currentTrack) {
        NavManager.setMiniPlayerView(navState.navRoute != PlayerView().route && currentTrack != null)
    }

    val animTopGradientIntensity = animateFloatAsState(
        if (isScrolledPastFirstItem) 1f else 0f,
        visibilityThreshold = 0.001f,
        animationSpec = tween(durationMillis = 400, delayMillis = 0)
    )

    LaunchedEffect(Unit) {
        NavManager.changeNavState(NavRoutes.Home())
    }

    LaunchedEffect(navState) {
        println("navChange ${navState.popBack} : ${navState.navRoute}")
        if (navState.popBack) {
            navController.popBackStack()

        } else {
            when (navState) {
                is Album -> navController.navigate(Album().route)
                is Home -> navController.navigate(Home().route)
                is Library -> navController.navigate(Library().route)
                is Search -> navController.navigate(Search().route)
                is WelcomeSearchAlbum -> navController.navigate(WelcomeSearchAlbum().route)
                is WelcomeLogin -> navController.navigate(WelcomeLogin().route)
                is WelcomeSetupAlbum -> navController.navigate(WelcomeSetupAlbum().route)
                is PlayerView -> {
                    NavManager.changePlayerViewState(PlayerFullViewState.PLAYING)
                    navController.navigate(PlayerView().route)
                }
            }
        }


    }


    val welcomeViewmodel: WelcomeViewmodel = viewModel(key = "WELCOME_VIEWMODEL")


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = surfaceColor
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Home().route// Define the initial screen
            ) {

                composable(route = NavRoutes.WelcomeLogin().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState.welcomeSubRoutes) {

                            WelcomeSubRoutes.LOGIN -> TODO()
                            WelcomeSubRoutes.SETUP -> {
                                NavManager.changeNavState(
                                    NavRoutes.WelcomeLogin(
                                        popBackStack = true
                                    )
                                )
                            }

                            WelcomeSubRoutes.SEARCH -> {
                                NavManager.changeNavState(
                                    NavRoutes.WelcomeSetupAlbum(
                                        popBackStack = true
                                    )
                                )
                            }

                            WelcomeSubRoutes.NONE -> {}
                        }
                    }
                    WelcomeScreen(
                        welcomeViewmodel = welcomeViewmodel,
                        topBarViewModel = topBarViewModel,
                        scrollPastFirstItem = {
                            NavManager.setTopBarVisibility(!it)
                        }
                    )
                }

                // Home Screen Destination
                composable(route = NavRoutes.Home().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState) {
                            else -> NavManager.changeNavState(NavRoutes.Home(popBackStack = true))
                        }
                    }
                    HomeScreen(
                        mainViewModel.homeViewModel,
                        scrollPastFirstItem = {
                            NavManager.setTopBarVisibility(!it)
                        }
                    )
                }

                // Profile Screen Destination with Arguments
                composable(route = NavRoutes.Album().route) { backStackEntry ->
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState) {
                            else -> NavManager.changeNavState(NavRoutes.Home(popBackStack = true))
                        }
                    }
                    // Reconstruct the typed object from the back stack entry
                    AlbumScreen(selectedAlbum!!, playerViewModel, {})
                }

                composable(route = NavRoutes.Search().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState) {
                            else -> NavManager.changeNavState(NavRoutes.Home(popBackStack = true))
                        }
                    }
                    HomeScreen(
                        mainViewModel.homeViewModel,
                        scrollPastFirstItem = {
                            NavManager.setTopBarVisibility(!it)
                        }
                    )
                }

                composable(route = NavRoutes.Library().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState) {
                            else -> NavManager.changeNavState(NavRoutes.Home(popBackStack = true))
                        }
                    }
                    HomeScreen(
                        mainViewModel.homeViewModel,
                        scrollPastFirstItem = {
                            NavManager.setTopBarVisibility(!it)
                        }
                    )
                }

                composable(route = PlayerView().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        println("Entries : ")
                        if (playerFullViewState == PlayerFullViewState.QUEUE || playerFullViewState == PlayerFullViewState.LYRICS) {
                            NavManager.changePlayerViewState(PlayerFullViewState.PLAYING)
                        } else {
                            when (navState) {
                                else -> NavManager.goBack(
                                    navController.previousBackStackEntry?.destination?.route
                                        ?: NavRoutes.Home().route
                                )
                            }
                        }

                    }
                    PlayerFullView(modifier = Modifier, playerViewModel)
                }

            }


            topBottomGradient(
                intensityTop = animTopGradientIntensity.value
            )

            TopBar(
                topBarViewModel,
            )


            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                visible = bottomBarVisible,
                enter = fadeIn(tween(durationMillis = 500, easing = LinearEasing)),
                exit = fadeOut(
                    tween(
                        durationMillis = 500,
                        delayMillis = 500,
                        easing = LinearEasing
                    )
                )
            ) {
                BottomBar(
                    welcomeViewmodel = welcomeViewmodel,
                    navController = navController,
                    modifier = Modifier
                )
            }

            //PlayerFullView(modifier = Modifier)
            //PlayerView(modifier = Modifier.align(Align      ment.BottomCenter))
            if (miniPlayerVisible) {
                PlayerView(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    playerViewModel
                )
            }


        }

    }
}

@Composable
private fun topBottomGradient(
    intensityTop: Float = 1f,
    intensityBottom: Float = 1f
) {
    val configuration = LocalWindowInfo.current.containerDpSize
    val density = LocalDensity.current

    val screenHeightDpFloat = with(density) { configuration.height.toPx() }


    Box(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(TitleColor.copy(alpha = 0.75f * intensityTop), Color.Transparent),
                    endY = screenHeightDpFloat * 0.15f
                )
            )
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, TitleColor.copy(alpha = 0.65f * intensityBottom)),
                    startY = screenHeightDpFloat * 0.9f
                )
            )
    )

}

@Preview
@Composable
fun previewNavScreen() {
    // NavScreen()
}