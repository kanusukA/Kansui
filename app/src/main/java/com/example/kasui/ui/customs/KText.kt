package com.example.kasui.ui.customs


import android.R.attr.scaleX
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.TextAutoSizeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.font.lerp
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.kasui.R
import com.example.kasui.ui.ViaodaLibre

@Composable
fun ThickenText(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean = false,
    scale: Float = 1f,
    baseStyle: TextStyle,
    colorAnim: Color? = null,
    duration: Int = 400
) {
    val animatedFloat = remember { Animatable(1f) }

    LaunchedEffect(key1 = selected) {
        if (selected) {
            animatedFloat.animateTo(1.3f, tween(duration))
        } else {
            animatedFloat.animateTo(1f, tween(duration))
        }

    }

    LaunchedEffect(scale) {
        animatedFloat.animateTo(targetValue = scale)
    }

    val colorFont = animateColorAsState(colorAnim ?: baseStyle.color)




    Text(
        modifier = modifier
            .graphicsLayer(
                scaleX = animatedFloat.value,
                scaleY = animatedFloat.value,
                transformOrigin = TransformOrigin(0f, 0f)
            ),
//                drawIntoCanvas { canvas ->
//                    val paintS = TextPaint().apply {
//                        isAntiAlias = true
//                        style = Paint.Style.STROKE
//                        strokeWidth = animatedFloat.value * strokeWidthScale
//                        color = colorFont.value.toArgb()
//                        textSize =
//                            baseStyle.fontSize.toPx() * ((animatedFloat.value * selectionFontScale) + 1f)
//                        typeface = typefaceViaoda
//                    }
//
//
//                    val paintF = TextPaint().apply {
//                        isAntiAlias = true
//                        style = Paint.Style.FILL
//                        color = colorFont.value.toArgb()
//                        textSize =
//                            baseStyle.fontSize.toPx() * ((animatedFloat.value * selectionFontScale) + 1f)
//                        typeface = typefaceViaoda
//
//
//                    }
//
//
//                    val staticLayoutS = StaticLayout.Builder
//                        .obtain(text, 0, text.length, paintS, size.width.toInt())
//                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
//                        .setLineSpacing(0f, 1f) // Optional: Adjust line spacing
//                        .setIncludePad(false)
//                        .build()
//
//                    val staticLayoutF = StaticLayout.Builder
//                        .obtain(text, 0, text.length, paintF, size.width.toInt())
//                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
//                        .setLineSpacing(0f, 1f) // Optional: Adjust line spacing
//                        .setIncludePad(false)
//                        .build()
//
//                    staticLayoutF.draw(canvas.nativeCanvas)
//                    staticLayoutS.draw(canvas.nativeCanvas)
//
//
//                }
//            },
        
        overflow = TextOverflow.Visible,
        text = text,
        style = baseStyle,
        color = colorFont.value
    )

//    Text(
//        modifier = modifier,
//        text = text,
//        style = androidx.compose.ui.text.lerp(
//            start = baseStyle,
//            stop = baseStyle.copy(
//                textMotion = TextMotion.Animated,
//                fontWeight = FontWeight(1000)
//            ),
//            fraction = animatedFloat.value
//
//        )
//    )
}