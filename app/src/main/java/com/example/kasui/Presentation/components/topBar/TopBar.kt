package com.example.kasui.Presentation.components.topBar

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.kasui.ui.ViaodaLibre


@Composable
fun TopBar(){
    var headingText by remember { mutableStateOf("Kasui") }
    var fontSize by remember { mutableIntStateOf(72) }
    val animatedFont = animateIntAsState(fontSize)

    // MAIN PAGE
    Row(modifier = Modifier.fillMaxWidth()
        .clickable(
            interactionSource = null,
            indication = null,onClick = {
            if (headingText == "Kasui"){
                headingText = "Search"
                fontSize = 52
            }else{
                headingText = "Kasui"
                fontSize = 72
            }
        })
    )
    {

        Text(

            headingText,
            fontFamily = ViaodaLibre,
           // fontSize = animatedFont.value.sp,
            letterSpacing = -4.sp,
            style = TextStyle(textMotion = TextMotion.Animated, fontSize = animatedFont.value.sp)
        )

    }
}

@Preview
@Composable
fun previewTopBar(){
    TopBar()
}