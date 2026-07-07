package com.example.kasui.Presentation.components.topBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.ViaodaLibre

@Composable
fun <T> SelectionBar(
    entries: Map<String, T>,
    fontSize: TextUnit = 76.sp,
    visibility: Float = 1.0f,
    onChanged: (T) -> Unit
) {
    val lazyState = rememberLazyListState()

    var selectedEntry by remember {
        mutableIntStateOf(0)
    }
    LaunchedEffect(selectedEntry) {
        lazyState.animateScrollToItem(selectedEntry)
    }
//
//    LaunchedEffect(lazyState.isScrollInProgress) {
//        if (lazyState.firstVisibleItemScrollOffset > lazyState.layoutInfo.visibleItemsInfo[lazyState.firstVisibleItemIndex].size / 2
//            && lazyState.firstVisibleItemIndex < lazyState.layoutInfo.totalItemsCount - 1
//        ) {
//            lazyState.animateScrollToItem(lazyState.firstVisibleItemIndex + 1)
//        } else if (lazyState.firstVisibleItemScrollOffset != 0) {
//            lazyState.animateScrollToItem(lazyState.firstVisibleItemIndex)
//        }
//        println(lazyState.firstVisibleItemScrollOffset)
////        if (lazyState.firstVisibleItemScrollOffset < lazyState.layoutInfo.visibleItemsInfo[lazyState.firstVisibleItemIndex].size / 2)
//    }
    val scrollState = rememberScrollState()

//    LaunchedEffect(scrollState.) { }

//    PrimaryScrollableTabRow(
//        selectedTabIndex = selectedEntry,
//        containerColor = Color.Transparent,
//        indicator = {},
//        divider = {},
//        edgePadding = 0.dp
//    ) {
//        entries.forEachIndexed { index, string ->
//            val selectionColor = animateColorAsState(
//                if (index == selectedEntry) TitleColor.copy(alpha = visibility) else TitleColor.copy(
//                    alpha = 0.65f * visibility
//                )
//            )
//
//            Text(
//                modifier = Modifier.clickable(
//                    onClick = {
//                        selectedEntry = index
//                    }
//                ),
//                text = string,
//                fontFamily = ViaodaLibre,
//                letterSpacing = (-4).sp,
//                style = TextStyle(
//                    textMotion = TextMotion.Animated,
//                    fontSize = fontSize,
//                    color = selectionColor.value,
//                    lineHeight = 52.sp
//                )
//            )
//
//            Spacer(modifier = Modifier.width(36.dp))
//
//        }
//        Spacer(modifier = Modifier.width(200.dp))
//    }

    LazyRow(
        state = lazyState
    ) {
        items(entries.count()) { index ->

            val key = entries.keys.toList()[index]

            val selectionColor = animateColorAsState(
                if (index == selectedEntry) TitleColor.copy(alpha = visibility) else TitleColor.copy(
                    alpha = 0.65f * visibility
                )
            )

            Text(
                modifier = Modifier.clickable(
                    onClick = {
                        selectedEntry = index
                        onChanged(entries.getValue(key))
                    }
                ),
                text = key,
                fontFamily = ViaodaLibre,
                letterSpacing = (-4).sp,
                style = TextStyle(
                    textMotion = TextMotion.Animated,
                    fontSize = fontSize,
                    color = selectionColor.value,
                    lineHeight = 52.sp
                )
            )

            Spacer(modifier = Modifier.width(36.dp))

        }
        item {
            Spacer(modifier = Modifier.width(200.dp))
        }
    }
}