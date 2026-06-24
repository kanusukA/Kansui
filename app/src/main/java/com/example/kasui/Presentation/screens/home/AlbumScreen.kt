package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Presentation.components.songCard.SongCard
import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor

@Composable
fun AlbumScreen(
    album: Album
) {

    val configuration = LocalWindowInfo.current.containerDpSize
    val density = LocalDensity.current

    val screenHeightDpFloat = with(density) { configuration.height.toPx() }

    val songList = MediaManager.fetchSongsFromAlbum(album)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = surfaceColor)
    ) {
        AsyncImage(
            model = album.albumAttributes.artwork.bitmap,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
//        Image(
//            painter = painterResource(R.drawable.cover),
//            contentDescription = null,
//            contentScale = ContentScale.FillWidth
//        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, surfaceColor),
                        endY = screenHeightDpFloat * 0.4f
                    )
                )
        )

        Text(
            modifier = Modifier.padding(top = 160.dp),
            text = album.albumAttributes.albumName,
            fontFamily = ViaodaLibre,
            fontSize = 68.sp,
            color = TitleColor,
            style = TextStyle(letterSpacing = -4.sp)
        )

        LazyColumn() {
            item {
                Spacer(modifier = Modifier.height(400.dp))
            }
            items(songList.size) { index ->

                    SongCard(
                        title = songList[index].attributes.name,
                        album = songList[index].attributes.albumName ?: "Unknown Album",
                        artist = songList[index].attributes.artistName ?: "Unknows Artist",
                        artwork = album.albumAttributes.artwork
                    )
                Spacer(Modifier.height(12.dp))
            }
        }


    }
}

@Preview
@Composable
fun previewAlbumScreen() {
    //AlbumScreen()
}