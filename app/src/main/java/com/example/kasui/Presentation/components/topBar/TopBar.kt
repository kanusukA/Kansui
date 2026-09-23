package com.example.kasui.Presentation.components.topBar


import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.PlayerFullViewState
import com.example.kasui.Presentation.screens.home.AlbumScreenState
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.textOnSurface
import com.example.kasui.viewmodels.Player
import com.example.kasui.viewmodels.TopBarSelectionState
import com.example.kasui.viewmodels.TopBarViewModel
import com.example.kasui.viewmodels.TopSelectionBars
import kotlinx.coroutines.async


@Composable
fun TopBar(
    topBarViewModel: TopBarViewModel,

    ) {

    val navState by topBarViewModel.navState.collectAsStateWithLifecycle()
    val mediaState by topBarViewModel.mediaState.collectAsStateWithLifecycle()
    val selectedAlbum by topBarViewModel.selectedAlbum.collectAsStateWithLifecycle()

    val username by topBarViewModel.username.collectAsStateWithLifecycle()

    val albumScreenState by NavManager.albumScreenState.collectAsStateWithLifecycle()
    val visibility by NavManager.topBarVisibility.collectAsStateWithLifecycle()

    val playingTrack by topBarViewModel.playerViewModel.currentTrack.collectAsStateWithLifecycle()

    val playerFullViewState by NavManager.playerViewState.collectAsStateWithLifecycle()

    val animatedVisibility = remember { Animatable(TitleColor) }
    var TopPadding by remember {
        mutableStateOf(0.dp)
    }
    val animatedTopPadding =
        animateDpAsState(TopPadding)

    var fontSizeScale by remember { mutableFloatStateOf(85f) }
    val animatedFontSizeScale =
        animateFloatAsState(fontSizeScale)

    var subFontSizeScale by remember { mutableFloatStateOf(45f) }
    val animatedSubFontSizeScale =
        animateFloatAsState(subFontSizeScale)

    val topBarSelectionState by topBarViewModel.topBarSelectionState.collectAsStateWithLifecycle()

    var headingText by remember(navState, mediaState) {
        mutableStateOf(
            value = "kansui"
        )
    }

    var subHeading by remember {
        mutableStateOf("")
    }

    var subSubHeading by remember {
        mutableStateOf("")
    }

//
//    val subText by remember(selectedAlbum) {
//        mutableStateOf(
//            selectedAlbum?.album?.artistName ?: ""
//        )
//    }

    val selectionMode by remember(topBarSelectionState) {
        mutableStateOf(topBarSelectionState != TopBarSelectionState.NONE)
    }

    val topSelectionBar: TopSelectionBars by remember {
        mutableStateOf(TopSelectionBars.WelcomeSelectionBar())
    }

    LaunchedEffect(albumScreenState) {
        TopPadding = when (albumScreenState) {
            AlbumScreenState.TRACK_VIEW -> {
                NavManager.setTopBarVisibility(visibility = false)
                180.dp
            }

            AlbumScreenState.COVER_VIEW -> {
                NavManager.setTopBarVisibility(visibility = true)
                0.dp
            }

            AlbumScreenState.DEFAULT -> {
                NavManager.setTopBarVisibility(visibility = true)
                180.dp
            }
        }
    }


    LaunchedEffect(navState, mediaState, playerFullViewState) {
        when (navState) {
            is NavRoutes.Album -> {
                headingText = selectedAlbum?.album?.albumName ?: ""
                subHeading = selectedAlbum?.album?.artistName ?: ""
                TopPadding = 180.dp
                NavManager.setTopBarVisibility(true)
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
                fontSizeScale = 85f
                subFontSizeScale = 45f
            }

            is NavRoutes.Home -> {
                headingText = "Kansui"
                TopPadding = 0.dp
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
                fontSizeScale = 85f
                subFontSizeScale = 45f
            }

            is NavRoutes.Library -> {
                headingText = "Kansui"
                TopPadding = 0.dp
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
                fontSizeScale = 85f
                subFontSizeScale = 45f
            }

            is NavRoutes.Search -> {
                headingText = "Search"
                TopPadding = 0.dp
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
                fontSizeScale = 85f
                subFontSizeScale = 45f
            }

            is NavRoutes.WelcomeLogin -> {
                headingText = "Welcome"
                TopPadding = 0.dp
                subHeading = if (username.isNotEmpty()) username else "Kansui"
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
            }

            is NavRoutes.WelcomeSearchAlbum -> {
                headingText = "Welcome"
                subHeading = ""
                TopPadding = 0.dp
                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.ALBUM_WELCOME)
            }

            is NavRoutes.WelcomeSetupAlbum -> {
                headingText = "Welcome"
                subHeading = if (username.isNotEmpty()) username else "Kansui"
                TopPadding = 0.dp

                topBarViewModel.setTopBarSelectionState(TopBarSelectionState.NONE)
            }

            is NavRoutes.PlayerView -> {
                when (playerFullViewState) {

                    PlayerFullViewState.QUEUE -> {
                        headingText = "Queue"
                        subHeading = ""
                        TopPadding = 0.dp
                        fontSizeScale = 72f
                        subFontSizeScale = 32f
                        subSubHeading = ""
                        NavManager.setTopBarVisibility(true)
                    }

                    else -> {
                        headingText = playingTrack?.name ?: ""
                        subHeading = playingTrack?.albumName ?: ""
                        TopPadding = 420.dp
                        fontSizeScale = 64f
                        subFontSizeScale = 32f
                        subSubHeading = playingTrack?.artistName ?: ""
                        NavManager.setTopBarVisibility(true)
                    }
                }
            }
        }
    }

    val animatedSubTextColor = remember { Animatable(TitleColor) }
    val animatedSubTextPos = remember { Animatable(130f) }
    val animatedSubSubHeading = remember { Animatable(180f) }



    LaunchedEffect(visibility) {
        if (!visibility) {
            async {
                animatedVisibility.animateTo(
                    Color.Transparent,
                    animationSpec = tween(durationMillis = 800, delayMillis = 700)
                )
            }
            async {
                animatedSubTextColor.animateTo(
                    textOnSurface,
                    animationSpec = tween(durationMillis = 800, delayMillis = 400)
                )
            }
            animatedSubTextPos.animateTo(
                0f,
                animationSpec = tween(durationMillis = 800, delayMillis = 400)
            )
        } else {
            async {
                animatedVisibility.animateTo(
                    TitleColor,
                    animationSpec = tween(durationMillis = 800, delayMillis = 0)
                )
            }
            async {
                animatedSubTextColor.animateTo(
                    TitleColor,
                    animationSpec = tween(durationMillis = 600, delayMillis = 0)
                )
            }
            animatedSubTextPos.animateTo(
                130f,
                animationSpec = tween(durationMillis = 600, delayMillis = 0)
            )
        }
    }


    // MAIN PAGE
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = animatedTopPadding.value)
    )
    {
        AnimatedContent(selectionMode) { mode ->
            if (!mode) {
                Text(
                    text = headingText,
                    fontFamily = ViaodaLibre,
                    letterSpacing = (-4).sp,
                    style = TextStyle(
                        textMotion = TextMotion.Animated,
                        fontSize = animatedFontSizeScale.value.sp,
                        color = animatedVisibility.value,
                        lineHeight = 52.sp
                    )
                )
            } else {
                SelectionBar(topSelectionBar.entries, onChanged = { state ->
                    topBarViewModel.setTopBarSelectionState(state)
                })

            }
        }

        // WELCOME SUBTEXT
//        AnimatedVisibility(
//            modifier = Modifier.offset(y = -38.dp),
//            visible = navState.navRoute == NavRoutes.WelcomeLogin().route
//        ) {
//            Column() {
//                Text(
//
//                    text = subHeading,
//                    fontFamily = ViaodaLibre,
//                    letterSpacing = (-4).sp,
//                    style = TextStyle(
//                        textMotion = TextMotion.Animated,
//                        fontSize = 38.sp,
//                        color = animatedVisibility.value,
//                        lineHeight = 48.sp
//
//                    )
//                )
//
//                Spacer(modifier = Modifier.height(animatedSubTextPos.value.dp))
//
//                AnimatedVisibility(
//                    visible = navState == NavRoutes.WelcomeSetupAlbum() || navState == NavRoutes.WelcomeSearchAlbum()
//                ) {
//                    Column {
//                        Text(
//                            modifier = Modifier.offset(y = -90.dp),
//                            text = if (navState == NavRoutes.WelcomeSetupAlbum()) "Let's set you up" else "Choose album",
//                            fontFamily = UncutSans,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 20.sp,
//                            color = animatedSubTextColor.value
//                        )
//                        Text(
//                            modifier = Modifier.offset(y = -90.dp),
//                            text = if (navState == NavRoutes.WelcomeSetupAlbum()) "From below select the albums you want to update"
//                            else "Select the album most accurate to the one searched for.",
//                            fontFamily = UncutSans,
//                            fontSize = 18.sp,
//                            color = animatedSubTextColor.value
//                        )
//                    }
//                }
//
//            }
//        }

        // ALBUM SUBTEXT
        AnimatedVisibility(
            modifier = Modifier.offset(y = -32.dp),
            visible = navState.navRoute == NavRoutes.Album().route || navState.navRoute == NavRoutes.PlayerView().route
        ) {
            Text(

                text = subHeading,
                fontFamily = ViaodaLibre,
                letterSpacing = (-4).sp,
                style = TextStyle(
                    textMotion = TextMotion.Animated,
                    fontSize = animatedSubFontSizeScale.value.sp,
                    color = animatedVisibility.value,
                    lineHeight = 48.sp

                )
            )
        }

        AnimatedVisibility(
            modifier = Modifier.offset(y = -48.dp),
            visible = navState.navRoute == NavRoutes.PlayerView().route
        ) {
            Text(

                text = subSubHeading,
                fontFamily = ViaodaLibre,
                letterSpacing = (-4).sp,
                style = TextStyle(
                    textMotion = TextMotion.Animated,
                    fontSize = 32.sp,
                    color = animatedVisibility.value,
                    lineHeight = 48.sp

                )
            )
        }

    }
}

@Preview
@Composable
fun previewTopBar() {
//    TopBar(visibility = true)
}