package com.example.kasui.Presentation.screens.home

import android.graphics.Bitmap
import android.graphics.Paint
import android.provider.CalendarContract
import android.text.style.BackgroundColorSpan
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import coil3.compose.AsyncImage
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.PlayerFullViewState
import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.customs.Trigger
import com.example.kasui.ui.customs.seeker
import com.example.kasui.ui.customs.slider
import com.example.kasui.ui.customs.ssspring
import com.example.kasui.ui.interlope
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.variantColor
import com.example.kasui.ui.variantHighColor
import com.example.kasui.viewmodels.Player
import com.example.kasui.viewmodels.PlayerListener
import com.example.kasui.viewmodels.PlayerStates
import com.example.kasui.viewmodels.PlayerViewModel
import kotlin.math.abs

@Composable
fun PlayerView(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {
    // Pill Space
    val playerState by PlayerListener.playerState.collectAsStateWithLifecycle()
    val trackQueue by playerViewModel.trackQueue.collectAsStateWithLifecycle()
    val currentTrack by playerViewModel.currentTrack.collectAsStateWithLifecycle()
    val currentAlbum by playerViewModel.currentAlbum.collectAsStateWithLifecycle()

    val progress by Player.progress.collectAsStateWithLifecycle(0f)
    val progressText by Player.ProgressText.collectAsStateWithLifecycle("0:00")

    val animatedProgress = animateFloatAsState(progress)

    val artwork = currentAlbum?.album?.artwork?.getBitmap(LocalContext.current)
        ?.collectAsStateWithLifecycle(null)?.value

    var nextVisibility by remember {
        mutableFloatStateOf(0f)
    }
    var previousVisibility by remember {
        mutableFloatStateOf(0f)
    }
    var trigger by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(trigger) {
        nextVisibility = trigger
        previousVisibility = -trigger

    }


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
                    NavRoutes.PlayerView()
                )
            })
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp),
            text = progressText,
            fontSize = 12.sp,
            fontFamily = UncutSans,
            color = variantHighColor,
            fontWeight = FontWeight.Bold
        )

        LinearWavyProgressIndicator(
            modifier = Modifier
                .offset(y = 4.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            progress = { animatedProgress.value },
            amplitude = { 0.8f },
            trackColor = surfaceColor,
            stroke = Stroke(width = 20f, cap = StrokeCap.Round),
            color = TitleColor,
            waveSpeed = 12.dp
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 60.dp)
                .alpha(nextVisibility),
            text = "Next",
            fontFamily = ViaodaLibre,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TitleColor
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 180.dp)
                .alpha(previousVisibility),
            text = "Previous",
            fontFamily = ViaodaLibre,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TitleColor
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
                model = artwork ?: R.drawable.cover,
                contentDescription = "Album Cover"
            )

//            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .ssspring(
                        trigger = { trigger = it },
                        onTrigger = {
                            when (it) {
                                Trigger.LEFT -> Player.onPrevious()
                                Trigger.RIGHT -> Player.onNext()
                            }
                        },
                        triggerThreshold = 50.dp
                    )

            ) {
                Text(
                    currentTrack?.name ?: "",
                    fontFamily = ViaodaLibre,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TitleColor
                )
                Text(
                    modifier = Modifier.padding(top = 22.dp),
                    text = currentTrack?.albumName ?: "",
                    fontFamily = ViaodaLibre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TitleColor
                )
                Text(
                    modifier = Modifier.padding(top = 40.dp),
                    text = currentTrack?.artistName ?: "",
                    fontFamily = ViaodaLibre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = TitleColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            AnimatedContent(playerState) { it ->
                when (it) {

                    PlayerStates.PLAYING -> {
                        PlayIcon(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    Player.pause()
                                }),
                            size = 40,
                            size2 = 40,
                            spacing = 8.dp
                        )
                    }

                    else -> {
                        PauseIcon(
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    Player.play()
                                }),
                            size = 48
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

        }
    }

}

@Composable
fun PlayerFullView(
    modifier: Modifier = Modifier,
    playerViewModel: PlayerViewModel
) {

    val playerState by PlayerListener.playerState.collectAsStateWithLifecycle()

    val trackQueue by playerViewModel.trackQueue.collectAsStateWithLifecycle()

    val currentAlbum by playerViewModel.currentAlbum.collectAsStateWithLifecycle()
    val currentTrack by playerViewModel.currentTrack.collectAsStateWithLifecycle()

    val playerFullViewState by NavManager.playerViewState.collectAsStateWithLifecycle()

    val player by Player.exoPlayer.collectAsStateWithLifecycle()

    // PROGRESS AND SEEK
    val progress by Player.progress.collectAsStateWithLifecycle(0f)
    val progressText by Player.ProgressText.collectAsStateWithLifecycle("")
    var seeking by remember {
        mutableStateOf(false)
    }
    var seekProgress by remember {
        mutableFloatStateOf(0f)
    }
    var seekProgressText by remember {
        mutableStateOf("0:00")
    }


    val animatedProgress =
        animateFloatAsState(if (seeking) seekProgress else progress, animationSpec = tween(400))

    val adaptiveTextSizeChange by remember(playerFullViewState) {
        mutableIntStateOf(
            when (playerFullViewState) {
                PlayerFullViewState.QUEUE -> 16
                PlayerFullViewState.LYRICS -> 18
                else -> 0
            }
        )
    }
    val albumTint by remember(playerFullViewState) {
        mutableStateOf(
            when (playerFullViewState) {
                PlayerFullViewState.QUEUE -> surfaceColor.copy(alpha = 0.85f)
                else -> Color.Transparent.copy(alpha = 0f)
            }
        )
    }

    val animAlbumTint = animateColorAsState(albumTint)

    val animAdaptiveTextSizeChange = animateIntAsState(adaptiveTextSizeChange)

    val animImageFade =
        animateFloatAsState(if (playerFullViewState == PlayerFullViewState.LYRICS) 0f else 1f)

    var currentTrackIndex by remember {
        mutableIntStateOf(0)
    }

    val queueState = rememberLazyListState()

    LaunchedEffect(playerFullViewState) {
        if (playerFullViewState == PlayerFullViewState.QUEUE) {
            queueState.animateScrollToItem(currentTrackIndex)
        }
    }

    LaunchedEffect(Unit, currentTrack) {
        currentTrackIndex = player?.currentMediaItemIndex ?: 0
    }

    val artwork =
        currentAlbum?.album?.artwork?.getBitmap(LocalContext.current)
            ?.collectAsStateWithLifecycle(null)?.value

    Box(
        Modifier
            .fillMaxSize()
            .background(color = surfaceColor)
    ) {

        AnimatedVisibility(visible = playerFullViewState == PlayerFullViewState.LYRICS) {
            LazyColumn() {
                item {
                    Spacer(modifier = Modifier.height(240.dp))
                }
                item {
                    Text(
                        text = currentTrack?.lyrics ?: "",
                        fontSize = 28.sp,
                        fontFamily = ViaodaLibre,
                        color = TitleDarkColor
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(360.dp))
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(560.dp + (animAdaptiveTextSizeChange.value * 5).dp)
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {

                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 32.dp)
                            .size(360.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .alpha(animImageFade.value),
                        model = artwork ?: R.drawable.cover,
                        contentDescription = "Album Cover",
                        colorFilter = ColorFilter.tint(
                            animAlbumTint.value,
                            blendMode = BlendMode.SrcAtop
                        )
                    )
                }
                // QUEUE
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.align(Alignment.Center),
                    visible = playerFullViewState == PlayerFullViewState.QUEUE,
                    enter = slideInHorizontally { it * 2 },
                    exit = slideOutHorizontally { it * 2 }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        state = queueState,
                        horizontalAlignment = Alignment.End
                    ) {
                        item { Spacer(modifier = Modifier.height(120.dp)) }

                        items(
                            trackQueue.size,
                            key = { return@items trackQueue[it].id }) { trackIndex ->
                            Spacer(modifier = Modifier.height(12.dp))

                            if (currentTrackIndex == trackIndex) {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .slider(
                                            onSlide = {},
                                            onEnd = {
                                                if (it) {
                                                    Player.removeTrackAt(trackIndex)
                                                }
                                            },
                                            threshold = 500f,
                                            color = TitleColor
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = null,
                                            onClick = {
                                                Player.seekToMediaItem(trackIndex)
                                            }),
                                    text = trackQueue[trackIndex].name,
                                    fontSize = 28.sp,
                                    fontFamily = ViaodaLibre,
                                    color = TitleColor,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .slider(
                                            onSlide = {},
                                            onEnd = {
                                                println("Before : index = $trackIndex")
                                                trackQueue.forEach { println(" ${it.name}") }
                                                if (it) {
                                                    Player.removeTrackAt(trackIndex)
                                                }

                                            },
                                            threshold = 500f,
                                            color = variantColor
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = null,
                                            onClick = {
                                                Player.seekToMediaItem(trackIndex)
                                            }),
                                    text = trackQueue[trackIndex].name,
                                    fontSize = 20.sp,
                                    fontFamily = UncutSans,
                                    color = variantColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                        }
                        item { Spacer(modifier = Modifier.height(120.dp)) }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Transparent,
                                        surfaceColor
                                    )
                                )
                            )
                    )
                }
            }

            // QUEUE
            LinearWavyProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .seeker(
                        progress,
                        seekOut = {
                            seekProgress = it
                            seekProgressText = Player.getSeekProgressToProgressText(it)
                        },
                        onStart = {
                            seekProgress = progress
                            seekProgressText = progressText
                            seeking = true
                        },
                        onEnd = {
                            Player.seekToProgress(seekProgress)
                            seeking = false
                        }
                    ),
                progress = {
                    if (seeking) seekProgress else animatedProgress.value
                },
                amplitude = { 0.8f },
                trackColor = surfaceHighColor,
                stroke = Stroke(width = 20f, cap = StrokeCap.Round),
                color = TitleColor,
                waveSpeed = 12.dp
            )

            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 12.dp),
                text = if (seeking) seekProgressText else progressText,
                fontSize = 18.sp,
                fontFamily = UncutSans,
                color = variantHighColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp - animAdaptiveTextSizeChange.value.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PreviousIcon(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {
                            Player.onPrevious()
                        }),
                    size = 72 - animAdaptiveTextSizeChange.value
                )

                AnimatedContent(playerState) { it ->
                    when (it) {

                        PlayerStates.PLAYING -> {
                            PlayIcon(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = null,
                                    onClick = {
                                        Player.pause()
                                    }),
                                size = 64 - animAdaptiveTextSizeChange.value,
                                spacing = 8.dp
                            )
                        }

                        else -> {
                            PauseIcon(
                                modifier = Modifier.clickable(
                                    indication = null,
                                    interactionSource = null,
                                    onClick = {
                                        Player.play()
                                    }),
                                size = 74 - animAdaptiveTextSizeChange.value
                            )
                        }
                    }
                }
                // PauseIcon(size = 64)

                NextIcon(
                    modifier = Modifier.clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {
                            Player.onNext()
                        }),
                    size = 72 - animAdaptiveTextSizeChange.value
                )
            }

            AnimatedVisibility(playerFullViewState == PlayerFullViewState.PLAYING) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                NavManager.changePlayerViewState(PlayerFullViewState.LYRICS)
                            }),
                        text = "Lyrics",
                        fontSize = 28.sp,
                        color = TitleColor,
                        fontWeight = FontWeight.Normal,
                        fontFamily = ViaodaLibre
                    )
                    Text(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = null,
                            onClick = {
                                NavManager.changePlayerViewState(PlayerFullViewState.QUEUE)
                            }),
                        text = "Queue",
                        fontSize = 28.sp,
                        color = TitleColor,
                        fontWeight = FontWeight.Normal,
                        fontFamily = ViaodaLibre
                    )
                }
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
fun HomeIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            "H",
            fontSize = size.sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
        Text(
            modifier = Modifier
//                .rotate(180f)
                .offset(y = -8.dp),
            text = "^",
            fontSize = (size + 32).sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.SemiBold,
            fontFamily = ViaodaLibre
        )
    }
}

@Composable
fun SearchIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            "O",
            fontSize = size.sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = ViaodaLibre
        )
        Text(
            modifier = Modifier
                .rotate(-20f)
                .offset(x = 7.dp, y = 15.dp),
            text = "\\",
            fontSize = (size + 24).sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.Black,
            fontFamily = ViaodaLibre
        )
    }
}

@Composable
fun LibraryIcon(
    modifier: Modifier = Modifier,
    size: Int
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "&",
            fontSize = (size).sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.Black,
            fontFamily = ViaodaLibre
        )
        Text(
            modifier = Modifier.offset(x = 1.dp, y = 7.dp),
            text = "S",
            fontSize = (size - 8).sp,
            color = TitleDarkColor,
            fontWeight = FontWeight.Black,
            fontFamily = ViaodaLibre
        )


    }
}

@Preview
@Composable
fun previewHomeIcon() {
    LibraryIcon(size = 24)
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