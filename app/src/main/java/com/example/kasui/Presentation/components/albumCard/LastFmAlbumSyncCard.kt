package com.example.kasui.Presentation.components.albumCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kasui.Data.LastFm.LastFmSearchAlbum
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.R
import com.example.kasui.ui.surfaceHighestColor
import org.jetbrains.annotations.Async

@Composable
fun LastFmSyncCard(
    rawAlbum: Album,
    searchResults: LastFmSearchAlbum,
    selectedAlbum: Int = 0
) {

    Column(
        modifier = Modifier.clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(color = surfaceHighestColor)
        ) {

            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = rawAlbum.albumAttributes.artwork.bitmap ?: R.drawable.cover,
                contentDescription = null
            )
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(rawAlbum.albumAttributes.albumName)
                Text(rawAlbum.albumAttributes.artistName)
            }

        }
        HorizontalDivider()
        if (!searchResults.albumMatches.isNullOrEmpty())


            LazyColumn() {
                items(count = searchResults.albumMatches.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(color = surfaceHighestColor)
                    ) {

                        AsyncImage(
                            modifier = Modifier.size(64.dp),
                            model = searchResults.albumMatches[index].image?.lastOrNull()?.url
                                ?: R.drawable.cover,
                            contentDescription = null
                        )
                        Column(verticalArrangement = Arrangement.SpaceEvenly) {
                            Text(searchResults.albumMatches[index].name ?: "Unknown")
                            Text(searchResults.albumMatches[index].artist ?: "Unknown")
                        }

                    }
                }
            }

    }

}