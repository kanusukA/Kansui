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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.NavState
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.viewmodels.TopBarViewModel


@Composable
fun TopBar(){

    val topBarViewModel: TopBarViewModel = viewModel()

    val navState by topBarViewModel.navState.collectAsStateWithLifecycle()

    val headingText by remember (navState){
        mutableStateOf(
        when(navState){

            NavState.SEARCH -> "Search"
            else -> "Kansui"
        }
        )
    }



    // MAIN PAGE
    Row(modifier = Modifier.fillMaxWidth()
        .clickable(
            interactionSource = null,
            indication = null,onClick = {

        })
    )
    {

        Text(
            headingText,
            fontFamily = ViaodaLibre,
           // fontSize = animatedFont.value.sp,
            letterSpacing = -4.sp,
            style = TextStyle(textMotion = TextMotion.Animated, fontSize = 72.sp)
        )

    }
}

@Preview
@Composable
fun previewTopBar(){
    TopBar()
}