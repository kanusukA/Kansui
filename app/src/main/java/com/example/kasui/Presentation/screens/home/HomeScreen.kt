package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.components.albumCard.AlbumCard
import com.example.kasui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    lazyState: LazyGridState = rememberLazyGridState()
) {
    val homeViewModel: HomeViewModel = viewModel()

    val albumList by homeViewModel.albumsList.collectAsStateWithLifecycle()

    val selectedAlbums = mutableListOf<Int>()

    LazyVerticalGrid(
        state = lazyState,
        columns = GridCells.Fixed(2)
    ) {
        //SPACER
        items(2) {
            Spacer(modifier = Modifier.height(120.dp))
        }

        items(albumList.size) { index ->
            AlbumCard(
                albumName = albumList[index].albumAttributes.albumName,
                artistName = albumList[index].albumAttributes.artistName,
                artwork = albumList[index].albumAttributes.artwork,
                onSelected = { bool ->
                    if (bool) {
                        selectedAlbums.add(index)
                    } else {
                        selectedAlbums.remove(index)
                    }
                },
                onClickSelection = selectedAlbums.isNotEmpty()
            )
        }
    }


}

@Preview
@Composable
fun previewHomeScreen() {
    HomeScreen()
}