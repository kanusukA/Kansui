package com.example.kasui.Presentation.components.bottomBar


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.NavState

import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.surfaceHighestColor
import com.example.kasui.ui.surfaceVariantColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.variantHighColor
import com.example.kasui.viewmodels.BottomBarViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {

    val bottomBarViewModel: BottomBarViewModel = viewModel()

    val navState by bottomBarViewModel.navState.collectAsStateWithLifecycle()

    var onHold by remember { mutableStateOf(false) }
    var dragPositionY by remember { mutableFloatStateOf(0f) }


    Row(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .background(surfaceHighColor, shape = CircleShape)

            .pointerInput(Unit) {

                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        onHold = true
                    },
                    onDrag = { change, dragAmount ->

                        dragPositionY = change.position.x
                    },
                    onDragEnd = { onHold = false },
                    onDragCancel = { onHold = false }
                )

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        BottomBarTab(
            R.drawable.home, "Home", navState == NavState.HOME,
            onHold = onHold,
            dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavState.HOME) })
        BottomBarTab(
            R.drawable.search, "Search", navState == NavState.SEARCH,
            onHold = onHold,
            dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavState.SEARCH) })
        BottomBarTab(
            R.drawable.library, "Library", navState == NavState.LIBRARY,
            onHold = onHold,
            dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavState.LIBRARY) })
    }

}

@Composable
private fun BottomBarTab(
    icon: Int,
    title: String,
    selected: Boolean,
    onHold: Boolean,
    dragPositionY: Float = 0f,
    onClick: () -> Unit
) {

    val animatedBGColor = animateColorAsState(
        if (selected)
            surfaceHighestColor
        else
            surfaceColor,
        animationSpec = tween(easing = EaseIn)
    )

    var itemPosWidth by remember { mutableStateOf(Offset(0f,0f)) }

    val onDragSelected by remember(dragPositionY) {
        derivedStateOf {
            dragPositionY in itemPosWidth.x..itemPosWidth.y
        }
    }
    LaunchedEffect(onDragSelected) {
        if (onDragSelected){
            onClick()
        }
    }

    val animatedDragPosition = animateDpAsState(if (onDragSelected && onHold) 24.dp else 0.dp)

    Box() {
        Row(
            modifier = Modifier
                .offset(0.dp,-animatedDragPosition.value)
                .onPlaced({
                    itemPosWidth = Offset(it.positionOnScreen().x,it.positionOnScreen().x + it.size.width.toFloat())

                })
                .clip(shape = CircleShape)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        if (down.pressed) {
                            onClick()
                        }
                    }
                }

                .background(color = animatedBGColor.value, shape = CircleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                Modifier
                    .size(48.dp),

                //            .background(color = surfaceColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(icon), contentDescription = null, tint = TitleColor)
            }
            AnimatedVisibility(selected) {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = title,
                    fontFamily = UncutSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TitleColor
                )
            }

        }
    }


}

@Preview
@Composable
fun previewBottomBar() {
    BottomBar()
}