package com.example.kasui.Presentation.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.components.songCard.SongCard
import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.textColor

@Composable
fun AlbumScreen(
    album: Album,
    onAlbumScreenState: (AlbumScreenState) -> Unit
) {

    val lazyState = rememberLazyListState()

    val configuration = LocalWindowInfo.current.containerDpSize
    val density = LocalDensity.current

    val screenHeightDpFloat = with(density) { configuration.height.toPx() }

    var albumScreenState: AlbumScreenState by remember {
        mutableStateOf(AlbumScreenState.DEFAULT)
    }

//    val songList = MediaManager.fetchSongsFromAlbum(album)
    val tracks by album.tracks.collectAsStateWithLifecycle(initialValue = emptyList())

    val isScrolledPastFirstItem by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 0 }
    }

    var gradientPositionY by remember(albumScreenState) {
        mutableFloatStateOf(
            when (albumScreenState) {
                AlbumScreenState.COVER_VIEW -> screenHeightDpFloat * 1f
                AlbumScreenState.TRACK_VIEW -> screenHeightDpFloat * 0f
                AlbumScreenState.DEFAULT -> screenHeightDpFloat * 0.55f
            }

        )
    }

    val animatedGradientY = animateFloatAsState(gradientPositionY)
    val animatedTrackColumnOffset =
        animateDpAsState(if (albumScreenState == AlbumScreenState.COVER_VIEW) 80.dp else 0.dp)

    LaunchedEffect(albumScreenState) {
        onAlbumScreenState(albumScreenState)
        NavManager.setAlbumScreenState(albumScreenState)
    }

    var imageSize by remember {
        mutableStateOf(300.dp)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image size based on scroll delta
                if (lazyState.layoutInfo.visibleItemsInfo.first().offset < 0) {
                    if (isScrolledPastFirstItem) {
                        albumScreenState = AlbumScreenState.TRACK_VIEW
                    } else {
                        albumScreenState = AlbumScreenState.DEFAULT
                    }

                    return Offset.Zero
                }
                val delta = available.y
                val newImageSize = imageSize + delta.dp
                val previousImageSize = imageSize


                // Constrain the image size within the allowed bounds
                imageSize = newImageSize.coerceIn(300.dp, 400.dp)
                val consumed = imageSize - previousImageSize

                if (imageSize == 400.dp) {
                    albumScreenState = AlbumScreenState.COVER_VIEW
                } else {
                    albumScreenState = AlbumScreenState.DEFAULT
                }

                // Calculate the scale for the image
                // imageScale = currentImageSize / maxImageSize

                // Return the consumed scroll amount
                return Offset(0f, consumed.value)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = animatedTrackColumnOffset.value)
            .background(color = album.album.artwork.bgColor ?: surfaceColor)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(top = 64.dp)
                .size(imageSize)
                .clip(RoundedCornerShape(20.dp))
                .align(alignment = Alignment.TopCenter),
            model = album.album.artwork.bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            clipToBounds = true
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = animatedTrackColumnOffset.value + (animatedTrackColumnOffset.value * 2))

            .nestedScroll(nestedScrollConnection)
    ) {


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            // Color.Transparent,
                            /*album.albumAttributes.artwork.bgColor ?:*/ surfaceColor
                        ),
                        endY = animatedGradientY.value
                    )
                )
        )



        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            state = lazyState
        ) {
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
            item {
                Spacer(modifier = Modifier.height(350.dp))
            }
            items(tracks.size) { index ->
                Text(
                    tracks[index].name,
                    fontFamily = UncutSans,
                    fontSize = 20.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(Modifier.height(12.dp))
            }
            item {
                Spacer(modifier = Modifier.height(300.dp))
            }
        }


    }
}


enum class AlbumScreenState {
    TRACK_VIEW,
    COVER_VIEW,
    DEFAULT
}

@Preview
@Composable
fun previewAlbumScreen() {
    //AlbumScreen()
}