package com.example.kasui.Data.structure

import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import coil3.Bitmap
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.textOnSurface

class Artwork(
    val height: Int,
    val width: Int,
    val url: String? = null,
    val uri: Uri? = null,
    val bitmap: Bitmap? = null,
    var bgColor: Color? = null,
    var textColor1: Color? = null,
    var textColor2: Color? = null, // opposite to textColor1
    val textColor3: String? = null,
    val textColor4: String? = null,
) {

    init {
        var rgb = mutableListOf(0f, 0f, 0f)

        if (bitmap != null) {
            val pixelsArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(
                pixelsArray,
                0,
                bitmap.width,
                0,
                0,
                bitmap.width,
                bitmap.height
            )
            for (pixel in pixelsArray) {
                val color = Color(pixel)

                rgb[0] += color.red
                rgb[1] += color.green
                rgb[2] += color.blue

            }

            rgb[0] = rgb[0] / pixelsArray.size
            rgb[1] = rgb[1] / pixelsArray.size
            rgb[2] = rgb[2] / pixelsArray.size

        }

        bgColor = Color(red = rgb[0], green = rgb[1], blue = rgb[2])

        if (bgColor!!.luminance() >= 0.4f) {
            textColor1 = TitleDarkColor
            textColor2 = textOnSurface
        } else {
            textColor1 = textOnSurface
            textColor2 = TitleDarkColor

        }

    }

}