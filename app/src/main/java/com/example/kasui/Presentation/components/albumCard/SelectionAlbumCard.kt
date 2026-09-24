package com.example.kasui.Presentation.components.albumCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.kasui.Data.structure.album.Album

@Composable
fun SelectionAlbumCard(
    mediaAlbum: Album,
    mbAlbum: Album
) {

    val tracks by mediaAlbum.tracks.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .height(104.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(52.dp),
                model = mediaAlbum.album.artwork.getBitmap(LocalContext.current)
                    .collectAsStateWithLifecycle(null).value,
                contentDescription = null
            )

            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(mediaAlbum.album.albumName)
                Text(mediaAlbum.album.artistName)
                Text(tracks.size.toString())
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
//            AsyncImage(
//                modifier = Modifier.size(52.dp),
//                model = mbAlbum.albu.artwork.url,
//                contentDescription = null
//            )
//
//            Column(verticalArrangement = Arrangement.SpaceEvenly) {
//                Text(mbAlbum.albumAttributes.albumName)
//                Text(mbAlbum.albumAttributes.artistName)
//                Text(mbAlbum.albumAttributes.trackCount.toString())
//            }
        }
    }

}