package com.example.kasui.Presentation.components.songCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.R
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighestColor

import com.example.kasui.ui.surfaceVariantColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.textOnSurface
import com.example.kasui.ui.variantColor

@Composable
fun SongCard(
    title: String,
    album: String,
    artist: String,
    albumView: Boolean = false,
    artwork: Artwork?
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(64.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        artwork?.bgColor ?: surfaceHighestColor,
                        surfaceColor.copy(alpha = 0.55f),
                        surfaceColor.copy(alpha = 0.65f)
                    )
                ), shape = RoundedCornerShape(22.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(shape = RoundedCornerShape(18.dp))
        ) {
            // Image(painterResource(R.drawable.cover),contentDescription = null)
//            AsyncImage(
//                modifier = Modifier.size(72.dp),
//                model = artwork?.getBitmap(LocalContext.current)?.collectAsStateWithLifecycle(null)?.value,
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                title,
                fontFamily = UncutSans,
                fontSize = 18.sp,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            if (!albumView) {
                Text(
                    album,
                    fontFamily = UncutSans,
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    artist,
                    fontFamily = UncutSans,
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview
@Composable
fun previewSongCard() {
    //SongCard()
}