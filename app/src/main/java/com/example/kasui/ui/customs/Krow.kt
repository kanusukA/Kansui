package com.example.kasui.ui.customs

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import kotlin.math.max
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Krow(
    modifier: Modifier,
    hiddenComposable: @Composable () -> Unit = {},
    visible: Boolean = false,
    hidden: Boolean = false,
    entry: EnterTransition = scaleIn() + slideInHorizontally(
        initialOffsetX = { it }),
    exitTransition: ExitTransition = scaleOut() + slideOutHorizontally(targetOffsetX = { it }),
    content: @Composable () -> Unit
) {

    var items by remember {
        mutableFloatStateOf(
            3f
        )
    }
    val animItems = animateFloatAsState(items, animationSpec = tween(durationMillis = 2000))
    var spacing by remember {
        mutableIntStateOf(0)
    }

    var animSpacing = animateIntAsState(spacing)


    var rowWidth by remember {
        mutableIntStateOf(0)
    }
//    var animRow = animateIntAsState(rowWidth, animationSpec = tween(durationMillis = 2000))

    val newItemAnim = remember() {
        Animatable(
            spacing,
            typeConverter = TwoWayConverter({ AnimationVector(it.toFloat()) }, { it.value.toInt() })
        )
    }

//    val animRowWidth = remember {
//        Animatable(
//            rowWidth,
//            typeConverter = TwoWayConverter({ AnimationVector(it.toFloat()) }, { it.value.toInt() })
//        )
//    }

    LaunchedEffect(items) {
//        println("new spacing : $spacing")
//        newItemAnim.animateTo(spacing, animationSpec = tween(durationMillis = 2000))
//        animRowWidth.animateTo(rowWidth, animationSpec = tween(durationMillis = 2000))
    }

//    LaunchedEffect(visible) {
//        items = if (visible) {
//            4f
//        } else {
//            3f
//        }
//    }


    Layout(
        modifier = modifier,
        content = {
            content()

            hiddenComposable()

        },

        ) { measurables, constraints ->
        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints)
        }

//        items = placeables.size

        rowWidth = 0
        var rowHeight = 0

        placeables.forEachIndexed { index, placeable ->
            if (hidden) {
                if (index == 3 && !visible) {
                    items = placeables.size.toFloat() + 1
                    rowHeight = max(rowHeight, placeable.height)
                } else {
                    items = placeables.size.toFloat() + 2
                    rowWidth += placeable.width
                    rowHeight = max(rowHeight, placeable.height)
                }
            } else {
                items = placeables.size.toFloat() + 1
                rowWidth += placeable.width
                rowHeight = max(rowHeight, placeable.height)
            }

        }

        val layoutHeight = constraints.maxHeight
        val layoutWidth = constraints.maxWidth


        // Spaced Evenly
        spacing = ((layoutWidth - rowWidth) / (items)).toInt()

        layout(layoutWidth, layoutHeight) {
            var xPosition = spacing

            placeables.forEachIndexed { index, placeable ->
                val yPosition = (layoutHeight - placeable.height) / 2


                placeable.placeRelative(x = xPosition, y = yPosition)

                xPosition += placeable.width + spacing

            }
        }
    }
}