package com.example.kasui.ui.customs.SentientTextBox

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class SentientTextBoxShape(
    val indentWidth: Dp,
    val indentHeight: Dp,
    val indentSpaceFromStart: Dp,
    val indentCornerRadius: Dp,
    val cornerRadius: Dp,

    ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        return Outline.Generic(
            textBoxMainRectPath(
                size = size,
                indentWidth = with(density) { indentWidth.toPx() },
                indentHeight = with(density) { indentHeight.toPx() },
                indentSpaceFromStart = with(density) { indentSpaceFromStart.toPx() },
                cornerRadius = CornerRadius(with(density) { cornerRadius.toPx() }),
                indentCornerRadius = CornerRadius(with(density) { indentCornerRadius.toPx() })
            )
        )
    }

    fun copy(
        indentWidth: Dp = this.indentWidth,
        indentHeight: Dp = this.indentHeight,
        indentSpaceFromStart: Dp = this.indentSpaceFromStart,
        indentCornerRadius: Dp = this.indentCornerRadius,
        cornerRadius: Dp = this.cornerRadius,
    ) = SentientTextBoxShape(
        indentWidth,
        indentHeight,
        indentSpaceFromStart,
        indentCornerRadius,
        cornerRadius
    )

}

private fun textBoxMainRectPath(
    size: Size,
    indentWidth: Float,
    indentHeight: Float,
    indentSpaceFromStart: Float,
    indentCornerRadius: CornerRadius,
    cornerRadius: CornerRadius
): Path {
    val cornerSize = Size(cornerRadius.x, cornerRadius.y)
    val halfCornerSize = cornerSize.height / 2

    val path = Path()

    path.moveTo(0f, size.height / 2)
    path.lineTo(0f, cornerRadius.x / 2)

    // Top Start ARC
    path.arcTo(
        Rect(Offset.Zero, size = cornerSize),
        180f,
        90f,
        false
    )


    path.lineTo(indentSpaceFromStart - indentCornerRadius.x, 0f)

    path.cubicTo(
        x1 = indentSpaceFromStart + indentCornerRadius.x,
        y1 = 0f,
        x2 = indentSpaceFromStart,
        y2 = indentHeight,
        x3 = indentSpaceFromStart + indentCornerRadius.x * 2,
        y3 = indentHeight
    )

    path.lineTo((indentWidth + indentSpaceFromStart) - indentCornerRadius.x, indentHeight)

    path.cubicTo(
        x1 = indentSpaceFromStart + indentCornerRadius.x + indentWidth,
        y1 = indentHeight,
        x2 = indentSpaceFromStart + indentWidth,
        y2 = 0f,
        x3 = indentSpaceFromStart + (indentCornerRadius.x * 2) + indentWidth,
        y3 = 0f
    )

    path.lineTo(size.width - halfCornerSize, 0f)

    // Top End Arc
    path.arcTo(
        Rect(
            Offset(size.width - cornerSize.width, 0f),
            cornerSize
        ),
        -90f,
        90f,
        false
    )

    path.lineTo(size.width, size.height - halfCornerSize)

    // Bottom End Arc
    path.arcTo(
        Rect(
            Offset(size.width - cornerSize.height, size.height - cornerSize.height),
            cornerSize
        ),
        0f,
        90f,
        false
    )

    path.lineTo(halfCornerSize, size.height)

    // BottomStart Arc
    path.arcTo(
        Rect(
            Offset(0f, size.height - cornerSize.height),
            cornerSize
        ),
        90f,
        90f,
        false
    )

    path.lineTo(0f, size.height / 2)

    return path
}

@Preview
@Composable
fun PreviewSentientTextBoxPath() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .height(90.dp)
                .width(300.dp)
        ) {
            drawPath(
                textBoxMainRectPath(
                    size,
                    300f,
                    40f,
                    120f,
                    CornerRadius(30f),
                    CornerRadius(160f),
                ),
                Color.Red,
                style = Stroke(10f)
            )
        }
    }
}