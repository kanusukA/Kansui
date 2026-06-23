package com.example.kasui.Presentation

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
import com.example.kasui.Presentation.components.bottomBar.BottomBar
import com.example.kasui.Presentation.components.topBar.TopBar
import com.example.kasui.Presentation.screens.home.HomeScreen
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.surfaceColor
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NavScreen() {

    val lazyState = rememberLazyGridState()

    val isScrolledPastFirstItem by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 0 }
    }
    val animTopGradientIntensity = animateFloatAsState(
        if (isScrolledPastFirstItem) 1f else 0f,
        visibilityThreshold = 0.001f,
        animationSpec = tween(durationMillis = 400, delayMillis = 0)
    )


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = surfaceColor
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                lazyState
            )

            topBottomGradient(
                intensityTop = animTopGradientIntensity.value
            )

            TopBar(visibility = !isScrolledPastFirstItem)

            BottomBar(
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
                    listOf(TitleColor.copy(alpha = 0.65f * intensityTop), Color.Transparent),
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
    NavScreen()
}