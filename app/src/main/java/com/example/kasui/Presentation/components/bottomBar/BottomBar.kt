package com.example.kasui.Presentation.components.bottomBar


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.NavState
import com.example.kasui.Presentation.WelcomeSubRoutes


import com.example.kasui.R
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.TitleDarkColor
import com.example.kasui.ui.UncutSans
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.customs.Krow
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.surfaceHighColor
import com.example.kasui.ui.surfaceHighestColor
import com.example.kasui.ui.surfaceVariantColor
import com.example.kasui.ui.textColor
import com.example.kasui.ui.variantHighColor
import com.example.kasui.viewmodels.BottomBarViewModel
import com.example.kasui.viewmodels.MainViewModel
import com.example.kasui.viewmodels.WelcomeViewmodel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BottomBar(
    welcomeViewmodel: WelcomeViewmodel,
    modifier: Modifier = Modifier
) {

    val bottomBarViewModel: BottomBarViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()

    val navState by bottomBarViewModel.navState.collectAsStateWithLifecycle()

    var onHold by remember { mutableStateOf(false) }
    var dragPositionY by remember { mutableFloatStateOf(0f) }

    var backButtonSize by remember {
        mutableStateOf(42.dp)
    }


    val showBackTab by remember(navState) {
        mutableStateOf(
            when (navState) {
                is NavRoutes.Album -> {
                    true
                }

                else -> false
            }
        )
    }
    LaunchedEffect(showBackTab) {
        if (showBackTab) {
            backButtonSize = 42.dp
        } else {
            println("scale low")
            backButtonSize = 0.dp
        }
    }

    @Composable
    fun HomeScreenTabs() {
        BottomBarTab(
            icon = R.drawable.home,
            title = "Home",
            selected = navState.navRoute == NavRoutes.Home().route,
            onHold = onHold,
            dragPositionY = dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavRoutes.Home()) })

        BottomBarTab(
            icon = R.drawable.search,
            title = "Search",
            selected = navState.navRoute == NavRoutes.Search().route,
            onHold = onHold,
            dragPositionY = dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavRoutes.Search()) })

        BottomBarTab(
            icon = R.drawable.library,
            title = "Library",
            selected = navState.navRoute == NavRoutes.Library().route,
            onHold = onHold,
            dragPositionY = dragPositionY,
            onClick = { bottomBarViewModel.onChangeNavState(NavRoutes.Library()) })
    }

    @Composable
    fun WelcomeScreenTabs() {
        if (navState.navRoute == NavRoutes.WelcomeLogin().route) {
            when (navState.welcomeSubRoutes) {
                WelcomeSubRoutes.LOGIN -> {
                    BottomBarTab(
                        icon = null,
                        title = "Sign Up",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = { })
                    BottomBarTab(
                        icon = null,
                        title = "Sign In",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = {
                            welcomeViewmodel.loginLastFm(mainViewModel)
                        })
                }

                WelcomeSubRoutes.SETUP -> {
                    BottomBarTab(
                        icon = null,
                        title = "Back",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = { })

                    BottomBarTab(
                        icon = null,
                        title = "Select",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = {
                            welcomeViewmodel.albumSyncLastFm()
                        })
                }

                WelcomeSubRoutes.SEARCH -> {
                    BottomBarTab(
                        icon = null,
                        title = "Back",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = {
                            NavManager.changeNavState(NavRoutes.WelcomeSetupAlbum())
                        })
                    BottomBarTab(
                        icon = null,
                        title = "Finalize",
                        selected = true,
                        onHold = onHold,
                        dragPositionY = dragPositionY,
                        onClick = {

                        })
                }

                WelcomeSubRoutes.NONE -> TODO()
            }

        }
    }



    Krow(
        modifier = modifier
            .requiredHeight(56.dp)
            .clip(shape = CircleShape)
            .background(surfaceHighColor.copy(alpha = 0.85f), shape = CircleShape)
            .pointerInput(Unit) {

                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        onHold = true
                    },
                    onDrag = { change, dragAmount ->

                        dragPositionY = change.position.x
                    },
                    onDragEnd = { onHold = false },
                    onDragCancel = { onHold = false }
                )

            },
        hiddenComposable = {

            if (navState.navRoute == NavRoutes.Album().route) {
                BottomBarTab(
                    modifier = Modifier,
                    R.drawable.arrow_back, "Back", true,
                    onHold = onHold,
                    dragPositionY,
                    onClick = {
                        bottomBarViewModel.onChangeNavState(NavRoutes.Home(popBackStack = true))
                    })
            }
        },
        visible = showBackTab,
        exitTransition = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 2000)
        )

    ) {
//        Spacer(modifier = Modifier.width(48.dp))
        when (navState.navRoute) {

            NavRoutes.WelcomeLogin().route -> WelcomeScreenTabs()
            else -> HomeScreenTabs()
        }

//        AnimatedVisibility(
//            showBackTab,
//            enter = scaleIn() + slideInHorizontally(
//                initialOffsetX = { it }),
//            exit = scaleOut() + slideOutHorizontally(targetOffsetX = { it })
//        ) {
//            BottomBarTab(
//                R.drawable.arrow_back, "Back", true,
//                onHold = onHold,
//                dragPositionY,
//                onClick = { bottomBarViewModel.onChangeNavState(NavRoutes.Home(popBackStack = true)) })
//        }
    }

}

@Composable
private fun BottomBarTab(
    modifier: Modifier = Modifier,
    icon: Int?,
    title: String,
    selected: Boolean,
    onHold: Boolean,
    dragPositionY: Float = 0f,
    onClick: () -> Unit
) {

    val animatedBGColor = animateColorAsState(
        if (selected)
            variantHighColor
        else
            surfaceColor,
        animationSpec = tween(easing = EaseIn)
    )
    val animatedTextColor = animateColorAsState(
        if (selected)
            surfaceHighColor
        else
            TitleDarkColor,
        animationSpec = tween(easing = EaseIn)
    )

    var itemPosWidth by remember { mutableStateOf(Offset(0f, 0f)) }

    val onDragSelected by remember(dragPositionY) {
        derivedStateOf {
            dragPositionY in itemPosWidth.x..itemPosWidth.y
        }
    }
    LaunchedEffect(onDragSelected) {
        if (onDragSelected) {
            onClick()
        }
    }

    val animatedDragPosition = animateDpAsState(if (onDragSelected && onHold) 24.dp else 0.dp)

    Box(modifier = modifier.requiredHeight(42.dp), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .height(42.dp)
                .offset(0.dp, -animatedDragPosition.value)
                .onPlaced({
                    itemPosWidth = Offset(
                        it.positionOnScreen().x,
                        it.positionOnScreen().x + it.size.width.toFloat()
                    )

                })
                .clip(shape = CircleShape)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        if (down.pressed) {
                            onClick()
                        }
                    }
                }

                .background(color = animatedBGColor.value, shape = CircleShape),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier.width(16.dp))
            if (icon != null) {
                Box(
                    Modifier
                        .size(42.dp),

                    //            .background(color = surfaceColor, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(icon),
                        contentDescription = null,
                        tint = animatedTextColor.value
                    )
                }
            }
            AnimatedVisibility(selected) {
                Text(
                    modifier = Modifier,
                    text = title,
                    fontFamily = ViaodaLibre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = animatedTextColor.value
                )
            }
            Spacer(modifier.width(16.dp))

        }
    }


}

@Preview
@Composable
fun previewBottomBar() {
    //BottomBar()
}