package com.example.kasui.Presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.request.MediaManagerState
import com.example.kasui.Presentation.components.albumCard.AlbumCard
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.customs.KButton
import com.example.kasui.ui.customs.SentientTextBox.SentientTextBox
import com.example.kasui.ui.interlope
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.variantColor
import com.example.kasui.viewmodels.MainViewModel
import com.example.kasui.viewmodels.TopBarViewModel

@RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
@Composable
fun WelcomeScreen(
    scrollPastFirstItem: (Boolean) -> Unit
) {

    val mainViewModel: MainViewModel = viewModel()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val lastFmState by mainViewModel.lastfmState.collectAsStateWithLifecycle()
    val mediaManagerState by mainViewModel.mediaManagerState.collectAsStateWithLifecycle()
    val loginUsername by mainViewModel.username.collectAsStateWithLifecycle()
    val rawAlbumList by mainViewModel.rawAlbums.collectAsStateWithLifecycle()

    val lazyState = rememberLazyGridState()

    val isScrolledPastFirstItem by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(isScrolledPastFirstItem) {
        scrollPastFirstItem(isScrolledPastFirstItem)
    }

    // IMPROVE ANIMATIONS
    val animatedSubTextPos = animateDpAsState(
        if (isScrolledPastFirstItem) 24.dp else 180.dp,
        animationSpec = tween(durationMillis = 800, delayMillis = 500)
    )
    val animatedSubTextColor =
        animateColorAsState(if (isScrolledPastFirstItem) variantColor else TitleColor)


    val selectedAlbumList = remember { mutableStateListOf<Int>() }

    @Composable
    fun loginBody(modifier: Modifier) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Log in using your Last.Fm account",
                fontFamily = UncutSans,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraLight,
                color = TitleColor
            )

            Spacer(modifier = Modifier.height(24.dp))

//            SentientTextBox(
//                textFieldValue = username,
//                labelText = "Username",
//                labelTextSize = 14,
//                indentHeight = 16.dp
//            ) { change ->
//                username = change
//
//            }
//
//            Spacer(modifier = Modifier.height(18.dp))
//
//            SentientTextBox(
//                textFieldValue = password,
//                labelText = "Password",
//                labelTextSize = 14,
//                indentHeight = 16.dp,
//                visualTransformation = PasswordVisualTransformation()
//            ) { change ->
//                password = change
//
//            }

            Spacer(modifier = Modifier.height(34.dp))

            Row(
                modifier = Modifier.width(240.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                KButton("Sign Up") {

                }
                KButton(if (lastFmState == LASTFM_STATE.LOGGING_IN) "Signing In" else "Sign In") {
                    if (lastFmState != LASTFM_STATE.LOGGING_IN) {
                        mainViewModel.loginLastFm(username, password)
                    }
                }
            }

        }
    }

    @Composable
    fun newUserScreen(modifier: Modifier) {

        Box(modifier = Modifier.fillMaxSize()) {
            if (rawAlbumList.isEmpty() && mediaManagerState == MediaManagerState.LOADING_RAW) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = "Loading",
                    fontSize = 52.sp,
                    fontFamily = UncutSans,
                    fontWeight = FontWeight.Bold,
                    color = surfaceHighColor

                )
            } else if (rawAlbumList.isEmpty()) {
                Text("No Media Found!")
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2), state = lazyState) {

                    item {
                        Spacer(modifier = Modifier.height(260.dp))
                    }
                    item {
                        Spacer(modifier = Modifier.height(260.dp))
                    }

                    items(count = rawAlbumList.size) { index ->
                        val album = rawAlbumList[index]

                        AlbumCard(
                            albumName = album.albumAttributes.albumName,
                            artistName = album.albumAttributes.artistName,
                            artwork = album.albumAttributes.artwork,
                            onClick = { },
                            selected = selectedAlbumList.contains(index),
                            onSelected = { bool ->
                                if (bool) {
                                    selectedAlbumList.add(index)
                                } else {
                                    selectedAlbumList.remove(index)
                                }
                            },
                            onClickSelection = selectedAlbumList.isNotEmpty(),
                            selectionCount = selectedAlbumList.indexOfFirst { it == index } + 1
                        )

                    }
                }
            }

            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    "Let's set you up",
                    fontFamily = UncutSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = animatedSubTextColor.value
                )
                Text(
                    "From below select the albums you want to update",
                    fontFamily = UncutSans,
                    fontSize = 18.sp,
                    color = animatedSubTextColor.value
                )

            }

        }


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = surfaceColor)
    ) {

        if (lastFmState != LASTFM_STATE.SIGNED_IN) {
            //loginBody(modifier = Modifier.align(Alignment.Center))
        } else {
            newUserScreen(modifier = Modifier.padding(top = animatedSubTextPos.value))
        }


        Column(
            modifier = Modifier.padding(start = 8.dp, top = 16.dp)
        ) {
//            Text(
//                "Welcome",
//                fontFamily = ViaodaLibre,
//                fontSize = 78.sp,
//                fontWeight = FontWeight.Bold,
//                color = TitleColor
//            )
//            Text(
//                modifier = Modifier
//                    .offset(y = -28.dp)
//                    .padding(start = 12.dp),
//                text = if (lastFmState == LASTFM_STATE.SIGNED_IN && loginUsername.isNotEmpty()) loginUsername else "Kansui",
//                fontFamily = ViaodaLibre,
//                fontSize = 32.sp,
//                fontWeight = FontWeight.ExtraLight,
//                color = TitleColor
//            )
        }


    }


}