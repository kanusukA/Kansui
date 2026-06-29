package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    album: Album,
    scrollPastFirstItem: (Boolean) -> Unit
) {

    val lazyState = rememberLazyListState()

    val configuration = LocalWindowInfo.current.containerDpSize
    val density = LocalDensity.current

    val screenHeightDpFloat = with(density) { configuration.height.toPx() }

    val songList = MediaManager.fetchSongsFromAlbum(album)

    val isScrolledPastFirstItem by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(isScrolledPastFirstItem) {
        scrollPastFirstItem(isScrolledPastFirstItem)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = album.albumAttributes.artwork.bgColor ?: surfaceColor)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = album.albumAttributes.artwork.bitmap,
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            album.albumAttributes.artwork.bgColor ?: surfaceColor
                        ),
                        endY = screenHeightDpFloat * 0.4f
                    )
                )
        )

//        Text(
//            modifier = Modifier.padding(top = 160.dp),
//            text = album.albumAttributes.albumName,
//            fontFamily = ViaodaLibre,
//            fontSize = 68.sp,
//            color = TitleColor,
//            style = TextStyle(letterSpacing = -4.sp)
//        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 12.dp),
            state = lazyState
        ) {
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
            item {
                Spacer(modifier = Modifier.height(300.dp))
            }
            items(songList.size) { index ->

                SongCard(
                    title = songList[index].attributes.name,
                    album = songList[index].attributes.albumName ?: "Unknown Album",
                    artist = songList[index].attributes.artistName ?: "Unknows Artist",
                    artwork = album.albumAttributes.artwork,
                    albumView = true
                )
                Spacer(Modifier.height(12.dp))
            }
            item {
                Spacer(modifier = Modifier.height(300.dp))
            }
        }


    }
}

@Preview
@Composable
fun previewAlbumScreen() {
    //AlbumScreen()
}