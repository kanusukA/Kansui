package com.example.kasui.Presentation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kasui.Presentation.NavRoutes.*
import com.example.kasui.Presentation.components.bottomBar.BottomBar
import com.example.kasui.Presentation.components.topBar.TopBar
import com.example.kasui.Presentation.screens.home.AlbumScreen
import com.example.kasui.Presentation.screens.home.HomeScreen
import com.example.kasui.Presentation.screens.home.WelcomeScreen
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.surfaceColor
import com.example.kasui.viewmodels.WelcomeViewmodel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
@Composable
fun NavScreen() {

    val navController = rememberNavController()

    val navState by NavManager.navStates.collectAsStateWithLifecycle()

    val selectedAlbum by NavManager.selectedAlbum.collectAsStateWithLifecycle()

    var isScrolledPastFirstItem by remember {
        mutableStateOf(false)
    }


    val animTopGradientIntensity = animateFloatAsState(
        if (isScrolledPastFirstItem) 1f else 0f,
        visibilityThreshold = 0.001f,
        animationSpec = tween(durationMillis = 400, delayMillis = 0)
    )

    LaunchedEffect(navState) {
        println("navChange ${navState.popBack}")
        if (navState.popBack) {
            navController.popBackStack()
        } else {
            when (navState) {
                is Album -> navController.navigate(Album().route)
                is Home -> navController.navigate(Home().route)
                is Library -> navController.navigate(Library().route)
                is Search -> navController.navigate(Search().route)
                is WelcomeSearchAlbum -> navController.navigate(WelcomeSetupAlbum().route)
                else -> {}
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
                startDestination = NavRoutes.WelcomeLogin().route// Define the initial screen
            ) {

                composable(route = NavRoutes.WelcomeLogin().route) {
                    WelcomeScreen(
                        welcomeViewmodel = welcomeViewmodel,
                        scrollPastFirstItem = {
                            isScrolledPastFirstItem = it
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
                        scrollPastFirstItem = {
                            isScrolledPastFirstItem = it
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
                    AlbumScreen(selectedAlbum!!, scrollPastFirstItem = {
                        isScrolledPastFirstItem = it
                    })
                }

                composable(route = NavRoutes.Search().route) {
                    BackHandler(enabled = true) {
                        println("POP back")
                        when (navState) {
                            else -> NavManager.changeNavState(NavRoutes.Home(popBackStack = true))
                        }
                    }
                    HomeScreen(
                        scrollPastFirstItem = {
                            isScrolledPastFirstItem = it
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
                        scrollPastFirstItem = {
                            isScrolledPastFirstItem = it
                        }
                    )
                }
//
//                composable(route = NavRoutes.Home().route) {
//                    HomeScreen(
//                        lazyState
//                    )
//                }
            }


            topBottomGradient(
                intensityTop = animTopGradientIntensity.value
            )

            TopBar(visibility = !isScrolledPastFirstItem)

            BottomBar(
                welcomeViewmodel = welcomeViewmodel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            )

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