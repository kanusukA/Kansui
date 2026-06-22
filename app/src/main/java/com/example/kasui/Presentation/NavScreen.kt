package com.example.kasui.Presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kasui.Presentation.components.topBar.TopBar
import com.example.kasui.Presentation.screens.home.HomeScreen

@Composable
fun NavScreen(){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeScreen()
        TopBar()
    }
}

@Preview
@Composable
fun previewNavScreen(){
    NavScreen()
}