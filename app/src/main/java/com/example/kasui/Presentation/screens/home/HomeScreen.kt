package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.components.albumCard.AlbumCard
import com.example.kasui.Presentation.components.albumCard.SelectionAlbumCard
import com.example.kasui.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    scrollPastFirstItem: (Boolean) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()

    val lazyState = rememberLazyGridState()

    val albumList by homeViewModel.albumsList.collectAsStateWithLifecycle()

    val searchAlbumResult by homeViewModel.albumResult.collectAsStateWithLifecycle()

    val selectedAlbums = remember {
        mutableStateListOf<Int>()
    }

    val isScrolledPastFirstItem by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(isScrolledPastFirstItem) {
        scrollPastFirstItem(isScrolledPastFirstItem)
    }

    LazyColumn(
        //state = lazyState,
        //columns = GridCells.Fixed(2)
    ) {
        //SPACER
        items(2) {
            Spacer(modifier = Modifier.height(120.dp))
        }

        items(albumList.size) { index ->
            val albumSelected by remember(selectedAlbums.size) {
                mutableStateOf(selectedAlbums.contains(index))
            }
            AlbumCard(
                albumName = albumList[index].albumAttributes.albumName,
                artistName = albumList[index].albumAttributes.artistName,
                artwork = albumList[index].albumAttributes.artwork,
                selected = albumSelected,
                onClick = {
                    homeViewModel.navToAlbumScreen(albumList[index])
                },
                onSelected = { bool ->
                    if (bool) {
                        selectedAlbums.remove(index)
                    } else {
                        selectedAlbums.add(index)
                    }
                },
                selectionCount = "",
                onClickSelection = selectedAlbums.isNotEmpty()
            )
        }

//        searchAlbumResult.forEach { (i, albums) ->
//
//            item {
//                SelectionAlbumCard(
//                    albumList[i],
//                    albums[0]
//                )
//            }
//        }


    }


}

@Preview
@Composable
fun previewHomeScreen() {
    // HomeScreen()
}