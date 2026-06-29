package com.example.kasui.Presentation.components.albumCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.kasui.Data.structure.album.Album

@Composable
fun SelectionAlbumCard(
    mediaAlbum: Album,
    mbAlbum: Album
) {
    Column(modifier = Modifier
        .height(104.dp)
        .fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(52.dp),
                model = mediaAlbum.albumAttributes.artwork.bitmap,
                contentDescription = null
            )

            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(mediaAlbum.albumAttributes.albumName)
                Text(mediaAlbum.albumAttributes.artistName)
                Text(mediaAlbum.albumAttributes.trackCount.toString())
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(52.dp),
                model = mbAlbum.albumAttributes.artwork.url,
                contentDescription = null
            )

            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(mbAlbum.albumAttributes.albumName)
                Text(mbAlbum.albumAttributes.artistName)
                Text(mbAlbum.albumAttributes.trackCount.toString())
            }
        }
    }

}