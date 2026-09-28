package com.example.kasui.ui.customs

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced

@Composable
fun Modifier.seeker(
    seekProgress: Float,
    seekOut: (progress: Float) -> Unit,
    onStart: () -> Unit,
    onEnd: () -> Unit
)
        : Modifier = composed {

    var width: Int = 0
    this
        .onPlaced {
            width = it.size.width
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = {
                    onStart()
                },
                onDragEnd = {
                    onEnd()
                },
                onDragCancel = {
                    onEnd()
                },
                onHorizontalDrag = { pointerInput, dragAmount ->
                    seekOut((pointerInput.position.x / width).coerceIn(0f, 1f))
                }
            )
        }
}