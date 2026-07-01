package com.example.kasui.Presentation.components.topBar


import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Data.request.MediaManagerState
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.NavState
import com.example.kasui.Presentation.WelcomeNavStage
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.textOnSurface
import com.example.kasui.viewmodels.TopBarViewModel
import kotlinx.coroutines.async


@Composable
fun TopBar(
    visibility: Boolean
) {

    val topBarViewModel: TopBarViewModel = viewModel()

    val navState by topBarViewModel.navState.collectAsStateWithLifecycle()
    val mediaState by topBarViewModel.mediaState.collectAsStateWithLifecycle()
    val selectedAlbum by topBarViewModel.selectedAlbum.collectAsStateWithLifecycle()

    val username by topBarViewModel.username.collectAsStateWithLifecycle()


    val animatedVisibility = remember { Animatable(TitleColor) }
    val animatedTopPadding =
        animateDpAsState(if (navState.navRoute == NavRoutes.Album().route) 180.dp else 0.dp)

    var fontSizeScale by remember { mutableFloatStateOf(85f) }
    val animatedFontSizeScale =
        animateFloatAsState(fontSizeScale)

    val headingText by remember(navState, mediaState) {
        mutableStateOf(

            when (navState) {
                is NavRoutes.Album -> {
                    fontSizeScale = 64f
                    selectedAlbum?.albumAttributes?.albumName ?: ""

                }

                is NavRoutes.Search -> "Search"
                is NavRoutes.WelcomeLogin -> "Welcome"
                is NavRoutes.WelcomeSetupAlbum -> "Welcome"

                else -> {
                    if (mediaState == MediaManagerState.LOADING_RAW) {
                        "Loading"
                    } else {
                        fontSizeScale = 85f
                        "Kansui"
                    }

                }

            }

        )
    }

    val subText by remember(selectedAlbum) {
        mutableStateOf(
            selectedAlbum?.albumAttributes?.artistName ?: ""
        )
    }

    val animatedSubTextColor = remember { Animatable(TitleColor) }
    val animatedSubTextPos = remember { androidx.compose.animation.core.Animatable(130f) }



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
        // WELCOME SUBTEXT
        AnimatedVisibility(
            modifier = Modifier.offset(y = -38.dp),
            visible = navState.navRoute == NavRoutes.WelcomeLogin().route
        ) {
            Column() {
                Text(

                    text = username,
                    fontFamily = ViaodaLibre,
                    letterSpacing = (-4).sp,
                    style = TextStyle(
                        textMotion = TextMotion.Animated,
                        fontSize = 38.sp,
                        color = animatedVisibility.value,
                        lineHeight = 48.sp

                    )
                )

                Spacer(modifier = Modifier.height(animatedSubTextPos.value.dp))

                Text(
                    modifier = Modifier.offset(y = -90.dp),
                    text = "Let's set you up",
                    fontFamily = UncutSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = animatedSubTextColor.value
                )
                Text(
                    modifier = Modifier.offset(y = -90.dp),
                    text = "From below select the albums you want to update",
                    fontFamily = UncutSans,
                    fontSize = 18.sp,
                    color = animatedSubTextColor.value
                )

            }
        }

        // ALBUM SUBTEXT
        AnimatedVisibility(
            modifier = Modifier.offset(y = -38.dp),
            visible = navState.navRoute == NavRoutes.Album().route
        ) {
            Text(

                text = subText,
                fontFamily = ViaodaLibre,
                letterSpacing = (-4).sp,
                style = TextStyle(
                    textMotion = TextMotion.Animated,
                    fontSize = 45.sp,
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
    TopBar(visibility = true)
}