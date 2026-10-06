package com.example.kasui.Presentation.components.songCard


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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.kasui.Data.LastFm.LastFmSearchTrackMatches
import com.example.kasui.Data.LastFm.LastFmTrack
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.song.Song

import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.surfaceHighestColor
import com.example.kasui.ui.variantColor

@Composable
fun LastFmTrackSyncCard(
    rawSong: Song,
    searchResults: List<LastFmTrack>,
    selectedTrack: Int,
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
//                model = rawSong.attributes.artwork?.getBitmap(LocalContext.current)
//                    ?.collectAsStateWithLifecycle(null)?.value ?: R.drawable.cover,
//                contentScale = ContentScale.FillHeight,
//                contentDescription = null
//            )
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(rawSong.attributes.name)
                Text(rawSong.attributes.artistName ?: "Unknown")
            }

        }
        HorizontalDivider()
        if (searchResults.isNotEmpty())

            LazyColumn(modifier = Modifier.height(210.dp)) {
                items(count = searchResults.size) { index ->

                    val animatedSelectionColor = animateColorAsState(
                        if (selectedTrack == index) {
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
                            model = searchResults[index].track.album?.image?.lastOrNull()?.url,
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                        Column(verticalArrangement = Arrangement.SpaceEvenly) {
                            Text(searchResults[index].track.name ?: "Unknown")
                            Text(searchResults[index].track.album?.title ?: "Unknown")
                            Text(searchResults[index].track.artist?.name ?: "Unknown")
                        }

                    }
                }
            }

    }

}