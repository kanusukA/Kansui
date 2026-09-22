package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo

import coil3.compose.AsyncImage
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.textColor
import com.example.kasui.viewmodels.HomeViewModel

@Composable
fun PlayerView(
    modifier: Modifier,
) {
    // Pill Space

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 74.dp)
            .requiredHeight(64.dp)
            .fillMaxWidth()
            .background(
                surfaceHighColor.copy(alpha = 0.95f),
                shape = RoundedCornerShape(
                    topStart = 36.dp,
                    bottomStart = 12.dp,
                    topEnd = 36.dp,
                    bottomEnd = 12.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    topStart = 36.dp,
                    bottomStart = 12.dp,
                    topEnd = 36.dp,
                    bottomEnd = 12.dp
                )
            )
            .clickable(interactionSource = null, indication = null, onClick = {
                NavManager.changeNavState(
                    NavRoutes.Player()
                )
            })
    ) {
        LinearWavyProgressIndicator(
            modifier = Modifier
                .offset(y = 4.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            progress = { 0.2f },
            amplitude = { 0.8f },
            trackColor = surfaceColor,
            stroke = Stroke(width = 20f, cap = StrokeCap.Round),
            color = TitleColor,
            waveSpeed = 12.dp
        )
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(46.dp)
                    .clip(CircleShape),
                model = R.drawable.cover,
                contentDescription = "Album Cover"
            )

//            Spacer(modifier = Modifier.width(6.dp))

            Box(modifier = Modifier.fillMaxHeight()) {
                Text(
                    "astrology girl",
                    fontFamily = ViaodaLibre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TitleColor
                )
                Text(
                    modifier = Modifier.padding(top = 22.dp),
                    text = "Long Nights and Wasted Affairs",
                    fontFamily = ViaodaLibre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TitleColor
                )
                Text(
                    modifier = Modifier.padding(top = 40.dp),
                    text = "Mind's Eye",
                    fontFamily = ViaodaLibre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TitleColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            PlayIcon(

                size = 40,
                size2 = 40,
                spacing = 8.dp
            )
            Spacer(modifier = Modifier.width(12.dp))

        }

    }
}

@Composable
fun PlayerFullView(
    modifier: Modifier = Modifier
) {

    Box(
        Modifier
            .fillMaxSize()
            .background(color = surfaceColor)
    ) {

        Column(
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CloseIcon(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 24.dp, bottom = 16.dp)
                    .clickable(onClick = { NavManager.changeNavState(NavRoutes.Home()) }),
                48
            )

            AsyncImage(
                modifier = Modifier
                    .size(360.dp)
                    .clip(RoundedCornerShape(24.dp)),
                model = R.drawable.cover,
                contentDescription = "Album Cover"
            )

            Spacer(modifier = Modifier.height(160.dp))

            LinearWavyProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { 0.2f },
                amplitude = { 0.8f },
                trackColor = surfaceHighColor,
                stroke = Stroke(width = 20f, cap = StrokeCap.Round),
                color = TitleColor,
                waveSpeed = 12.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PreviousIcon(size = 72)

                PlayIcon(size = 72)
                // PauseIcon(size = 64)

                NextIcon(size = 72)
            }
        }

    }

}

@Composable
fun CloseIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Text(
        modifier = modifier,
        text = "x",
        fontSize = size.sp,
        color = TitleColor,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = ViaodaLibre
    )
}

@Composable
fun PlayIcon(
    modifier: Modifier = Modifier,
    size: Int,
    size2: Int = size + 24,
    spacing: Dp = 12.dp
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            modifier = Modifier.padding(end = spacing, top = 6.dp),
            text = "I",
            fontSize = size.sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
        Text(
            modifier = Modifier.padding(start = spacing),
            text = ">",
            fontSize = size2.sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
    }

}

@Composable
fun NextIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Text(
        modifier = modifier,
        text = ">>",
        fontSize = size.sp,
        color = TitleColor,
        letterSpacing = -22.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = ViaodaLibre
    )
}

@Composable
fun PreviousIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Text(
        modifier = modifier,
        text = "<<",
        fontSize = size.sp,
        color = TitleColor,
        letterSpacing = -22.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = ViaodaLibre
    )
}

@Composable
fun PauseIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Text(
        modifier = modifier,
        text = "II",
        fontSize = size.sp,
        color = TitleColor,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = ViaodaLibre
    )
}

@Composable
fun ShuffleIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(90f),
            text = "8",
            fontSize = size.sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(-90f),
            text = "8",
            fontSize = size.sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
    }
}

@Composable
fun AntiClockIcon(
    size: Int
) {
    Box() {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "3",
            fontSize = (size + 36).sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(color = surfaceColor)
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 19.dp),
            text = "<",
            fontSize = size.sp,
            color = TitleColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )

    }
}