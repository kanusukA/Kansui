package com.example.kasui.Presentation.components.topBar


import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.NavState
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.viewmodels.TopBarViewModel


@Composable
fun TopBar(
    visibility: Boolean
) {

    val topBarViewModel: TopBarViewModel = viewModel()

    val navState by topBarViewModel.navState.collectAsStateWithLifecycle()

    val headingText by remember(navState) {
        mutableStateOf(
            when (navState) {

                NavState.SEARCH -> "Search"
                else -> "Kansui"
            }
        )
    }


    val animatedVisibility = remember { Animatable(TitleColor) }

    LaunchedEffect(visibility) {
        if (!visibility){
            animatedVisibility.animateTo(Color.Transparent, animationSpec = tween(durationMillis = 800, delayMillis = 700))
        }else{
            animatedVisibility.animateTo(TitleColor, animationSpec = tween(durationMillis = 800, delayMillis = 0))
        }
    }


    // MAIN PAGE
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    )
    {
        Text(
            headingText,
            fontFamily = ViaodaLibre,
            letterSpacing = -4.sp,
            style = TextStyle(
                textMotion = TextMotion.Animated,
                fontSize = 84.sp,
                color = animatedVisibility.value
            )
        )

    }
}

@Preview
@Composable
fun previewTopBar() {
    TopBar(visibility = true)
}