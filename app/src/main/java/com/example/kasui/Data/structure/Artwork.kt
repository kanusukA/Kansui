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
    val url: String?,
    val uri: Uri?,
    val bitmap: Bitmap?,
    val bgColor: String?,
    var textColor1: Color?,
    var textColor2: Color?, // opposite to textColor1
    val textColor3: String?,
    val textColor4: String?,
) {

    init {
        var rgb = mutableListOf(0f, 0f, 0f)

        if (bitmap != null) {
            val pixelsArray = IntArray(bitmap.width * (bitmap.height * 0.4).toInt())
            bitmap.getPixels(
                pixelsArray,
                0,
                bitmap.width,
                0,
                (bitmap.height * 0.6).toInt(),
                bitmap.width,
                (bitmap.height * 0.4).toInt()
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

//            for (h in (bitmap.height * 0.4).toInt()..<bitmap.height) {
//                for (w in 0..<bitmap.width) {
//                    rgb[0] += bitmap.getColor(w, h).red()
//                    rgb[1] += bitmap.getColor(w, h).green()
//                    rgb[2] += bitmap.getColor(w, h).blue()
//                    count += 1
//                }
//            }
        }

        textColor1 = Color(red = rgb[0], green = rgb[1], blue = rgb[2])

        if (textColor1!!.luminance() >= 0.4f) {
            textColor1 = TitleDarkColor
            textColor2 = textOnSurface
        } else {
            textColor1 = textOnSurface
            textColor2 = TitleDarkColor
            
        }

    }

}