package com.example.kasui.ui.customs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.kasui.ui.surfaceColor
import kotlinx.coroutines.launch
import kotlin.math.abs


@Composable
fun Modifier.slider(
    onSlide: (offset: Float) -> Unit,
    onEnd: (thresholdTrigger: Boolean) -> Unit,
    threshold: Float,
    color: Color = surfaceColor,
    triggerColor: Color = Color.Red
): Modifier = composed {
    val screenWidth = LocalWindowInfo.current.containerSize.width.toFloat()
    var xOffset by remember {
        mutableFloatStateOf(0f)
    }
    val animOffset = animateFloatAsState(xOffset)
    this
        .graphicsLayer {
            translationX = animOffset.value
            this.colorFilter = ColorFilter.tint(
                color = lerp(color, triggerColor, (abs(xOffset) / threshold).coerceIn(0f, 1f))
            )

        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = {

                },
                onDragEnd = {
                    if (abs(xOffset) > threshold) {
                        if (xOffset < 0) {
                            xOffset = -(screenWidth * 1.5f)
                        } else {
                            xOffset = screenWidth
                        }
                        onEnd(true)
                    } else {
                        xOffset = 0f
                        onEnd(false)
                    }
                },
                onDragCancel = {
                    xOffset = 0f
                    onEnd(false)
                },
                onHorizontalDrag = { pointerInput, _ ->
                    xOffset += pointerInput.positionChange().x
                    pointerInput.consume()
                }
            )
        }
}

@Composable
fun Modifier.ssspring(
    trigger: (Float) -> Unit,
    onTrigger: (trigger: Trigger) -> Unit,
    triggerThreshold: Dp = 80.dp,
    resistanceFactor: Float = 0.4f,
    springStiffness: Float = Spring.StiffnessHigh
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var isTriggered by remember { mutableStateOf(false) }
    var dragEnd by remember { mutableStateOf(false) }

    this
        .graphicsLayer {
            translationX = offsetX.value
        }
        .pointerInput(Unit) {
            val thresholdPx = triggerThreshold.toPx()

            detectHorizontalDragGestures(
                onDragStart = {
                    isTriggered = false
                    dragEnd = false
                },
                onDragEnd = {
                    dragEnd = true
                    trigger(0f)
                    coroutineScope.launch {
                        offsetX.animateTo(
                            0f,
                            animationSpec = spring(
                                stiffness = springStiffness
                            )
                        )
                    }
                    println("triggerend")

                },
                onDragCancel = {
                    dragEnd = true
                    trigger(0f)
                    coroutineScope.launch {
                        offsetX.animateTo(
                            0f,
                            animationSpec = spring(
                                stiffness = springStiffness
                            )
                        )
                    }
                    println("triggerend")

                },
                onHorizontalDrag = { pChange, dragAmount ->

                    coroutineScope.launch {
                        if (!dragEnd) {
                            val currentOffset = offsetX.value
                            val newOffset = currentOffset + (dragAmount * resistanceFactor)
                            offsetX.snapTo(newOffset)
                            val percent = newOffset / thresholdPx
                            if (!isTriggered) {
                                trigger(percent)
                                if (abs(percent) > 0.9) {
                                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                    onTrigger(if (percent > 0) Trigger.RIGHT else Trigger.LEFT)
                                    isTriggered = true
                                }

                            }

                        }

                    }

                }
            )
        }
}

enum class Trigger {
    LEFT,
    RIGHT
}

//fun Modifier.stiffSpringDrag(
//    onTrigger: () -> Unit,
//    triggerThreshold: Dp = 100.dp,
//    resistanceFactor: Float = 0.4f,
//    springStiffness: Float = Spring.StiffnessHigh
//): Modifier = composed {
//    val coroutineScope = rememberCoroutineScope()
//    val offsetX = remember { Animatable(0f) }
//    var isTriggered by remember { mutableStateOf(false) }
//
//    this
//        .graphicsLayer {
//            translationX = offsetX.value
//        }
//        .pointerInput(Unit) {
//            val thresholdPx = triggerThreshold.toPx()
//
//            detectHorizontalDragGestures(
//                onDragStart = {
//                    isTriggered = false
//                },
//                onDragEnd = {
//                    coroutineScope.launch {
//                        offsetX.animateTo(
//                            targetValue = 0f,
//                            animationSpec = spring(
//                                dampingRatio = Spring.DampingRatioMediumBouncy,
//                                stiffness = springStiffness
//                            )
//                        )
//                    }
//                },
//                onDragCancel = {
//                    coroutineScope.launch {
//                        offsetX.animateTo(
//                            targetValue = 0f,
//                            animationSpec = spring(stiffness = springStiffness)
//                        )
//                    }
//                },
//                onHorizontalDrag = { _, dragAmount ->
//                    coroutineScope.launch {
//                        val currentOffset = offsetX.value
//                        val newOffset = currentOffset + (dragAmount * resistanceFactor)
//                        offsetX.snapTo(newOffset)
//
//                        if (abs(newOffset) >= thresholdPx && !isTriggered) {
//                            isTriggered = true
//                            onTrigger()
//                        }
//                    }
//                }
//            )
//        }
//}
//Usage Example
//You can now apply .stiffSpringDrag(...) directly to any standard Composable:
//
//Kotlin
//@Composable
//fun StiffSpringDemo() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .size(120.dp)
//                .stiffSpringDrag(
//                    onTrigger = {
//                        println("Spring Triggered!")
//                    },
//                    triggerThreshold = 120.dp,
//                    resistanceFactor = 0.3f, // Super stiff resistance
//                    springStiffness = Spring.StiffnessHigh
//                ),
//            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "Drag Me", color = MaterialTheme.colorScheme.onPrimary)
//            }
//        }
//    }
//}