package com.example.kasui.Presentation.components.albumCard

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kasui.Data.LastFm.LastFmSearchAlbumMatch
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.R
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.surfaceHighestColor
import com.example.kasui.ui.variantColor

@Composable
fun LastFmSyncCard(
    rawAlbum: Album,
    searchResults: LastFmSearchAlbumMatch,
    selectedAlbums: Int,
    onChangeSelection: (Int) -> Unit
) {


    Column(
        modifier = Modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = surfaceHighColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = surfaceHighestColor)
        ) {

//            AsyncImage(
//                modifier = Modifier.size(70.dp),
//                model = rawAlbum.albumAttributes.artwork.bitmap ?: R.drawable.cover,
//                contentScale = ContentScale.FillHeight,
//                contentDescription = null
//            )
//            Column(verticalArrangement = Arrangement.SpaceEvenly) {
//                Text(rawAlbum.albumAttributes.albumName)
//                Text(rawAlbum.albumAttributes.artistName)
//            }

        }
        HorizontalDivider()
        if (!searchResults.album.isNullOrEmpty())

            LazyColumn(modifier = Modifier.height(210.dp)) {
                items(count = searchResults.album.size) { index ->

                    val animatedSelectionColor = animateColorAsState(
                        if (selectedAlbums == index) {
                            variantColor
                        } else {
                            surfaceHighestColor
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .height(70.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable(onClick = {
                                onChangeSelection(index)
                            })
                            .background(color = animatedSelectionColor.value)


                    ) {

                        AsyncImage(
                            modifier = Modifier.size(70.dp),
                            model = searchResults.album[index].image?.lastOrNull()?.url,
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                        Column(verticalArrangement = Arrangement.SpaceEvenly) {
                            Text(searchResults.album[index].name ?: "Unknown")
                            Text(searchResults.album[index].artist ?: "Unknown")
                        }

                    }
                }
            }

    }

}