package com.example.kasui.Presentation.components.albumCard


import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.Bitmap
import coil3.compose.AsyncImage
import coil3.toCoilUri
import com.example.kasui.R
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Presentation.components.Selectable
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.variantColor

@Composable
fun AlbumCard(
    albumName: String,
    artistName: String,
    artwork: Artwork?,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    selected: Boolean = false,
    onSelected: (Boolean) -> Unit = {},
    onClickSelection: Boolean,
    selectionCount: String = "0",
) {


    val animatedSelectionColor =
        animateColorAsState(if (selected) TitleColor.copy(alpha = 0.35f) else Color.Transparent)

    val art = artwork?.getBitmap(LocalContext.current)
        ?.collectAsStateWithLifecycle(null)?.value

    Box(
        modifier = Modifier
            .requiredSize(170.dp)
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onLongClick = {
                    onLongClick()
                    onSelected(selected)
                },
                onClick = {
                    if (onClickSelection) {
                        onSelected(selected)
                    } else {
                        onClick()
                    }
                }
            )

    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .requiredSize(160.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(24.dp))
                .clip(shape = RoundedCornerShape(size = 24.dp))
                .graphicsLayer(
                    colorFilter = ColorFilter.tint(
                        animatedSelectionColor.value,
                        blendMode = BlendMode.Plus
                    )
                )

        ) {
            AsyncImage(
                modifier = Modifier.size(170.dp),
                model = art ?: R.drawable.cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                clipToBounds = true,
            )

        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp),
            visible = selected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(color = variantColor.copy(alpha = 0.75f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    selectionCount,
                    fontFamily = ViaodaLibre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = surfaceColor

                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = albumName,
                fontFamily = UncutSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = artwork?.textColor1 ?: textColor,
                style = TextStyle(
                    shadow = Shadow(
                        artwork?.textColor2 ?: Color.White,
                        Offset(x = 2f, y = 2f),
                        blurRadius = 4f
                    )
                )
            )
            Text(
                text = artistName,
                fontFamily = UncutSans,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = artwork?.textColor1 ?: textColor,
                style = TextStyle(
                    shadow = Shadow(
                        artwork?.textColor2 ?: Color.White,
                        Offset(x = 2f, y = 2f),
                        blurRadius = 2f
                    )
                )

            )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}


@Preview
@Composable
fun previewAlbumCard() {
    // AlbumCard()
}