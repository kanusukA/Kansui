package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kasui.Presentation.components.albumCard.AlbumCard

@Composable
fun HomeScreen(){

    LazyVerticalGrid (columns = GridCells.Fixed(2)) {
        //SPACER
        items(2){
            Spacer(modifier = Modifier.height(120.dp))
        }

        items(15){
            AlbumCard()
        }
    }




}

@Preview
@Composable
fun previewHomeScreen(){
    HomeScreen()
}