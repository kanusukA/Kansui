package com.example.kasui.Data.structure

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.room.Entity
import androidx.room.Ignore
import coil3.Bitmap
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.textOnSurface
import com.example.kasui.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


data class Artwork(
    var height: Int = 0,
    var width: Int = 0,
    var url: String? = null,
    var uri: Uri? = null,
    @Ignore var notFound: Boolean = false,
    var bgColor: Color? = null,
    var textColor1: Color? = null,
    var textColor2: Color? = null, // opposite to textColor1
    var textColor3: String? = null,
    var textColor4: String? = null,
) {

    @Ignore
    private var _bitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    @Ignore
    val bitmap = _bitmap.asStateFlow()

    fun getBitmap(context: Context) {
        if (_bitmap.value == null && uri != null) {
            _bitmap.update { MediaManager.fetchArtworkFromTrackUri(context, uri!!) }
            if (_bitmap.value == null) {
                notFound = true
            }
        }

    }


    fun calColor() {
        var rgb = mutableListOf(0f, 0f, 0f)

        if (bitmap != null) {
            val pixelsArray = IntArray(bitmap.value!!.width * bitmap.value!!.height)
            bitmap.value!!.getPixels(
                pixelsArray,
                0,
                bitmap.value!!.width,
                0,
                0,
                bitmap.value!!.width,
                bitmap.value!!.height
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