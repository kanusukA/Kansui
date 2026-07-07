package com.example.kasui.Presentation.components.topBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasui.ui.TitleColor
import com.example.kasui.ui.ViaodaLibre

@Composable
fun SelectionBar(
    entries: List<String>,
    fontSize: TextUnit = 76.sp,
    visibility: Float = 1.0f,

    ) {
    val lazyState = rememberLazyListState()

    var selectedEntry by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(lazyState.isScrollInProgress) {
        if (lazyState.firstVisibleItemScrollOffset > lazyState.layoutInfo.visibleItemsInfo[lazyState.firstVisibleItemIndex].size / 2
            && lazyState.firstVisibleItemIndex < lazyState.layoutInfo.totalItemsCount
        ) {
            lazyState.animateScrollToItem(lazyState.firstVisibleItemIndex + 1)
        }
        println(lazyState.firstVisibleItemScrollOffset)
//        if (lazyState.firstVisibleItemScrollOffset < lazyState.layoutInfo.visibleItemsInfo[lazyState.firstVisibleItemIndex].size / 2)
    }

    LazyRow(
        state = lazyState
    ) {
        items(entries.count()) { index ->
            val selectionColor = animateColorAsState(
                if (index == selectedEntry) TitleColor.copy(alpha = visibility) else TitleColor.copy(
                    alpha = 0.65f * visibility
                )
            )
            Text(
                text = entries[index],
                fontFamily = ViaodaLibre,
                letterSpacing = (-4).sp,
                style = TextStyle(
                    textMotion = TextMotion.Animated,
                    fontSize = fontSize,
                    color = selectionColor.value,
                    lineHeight = 52.sp
                )
            )
            Spacer(modifier = Modifier.width(48.dp))
        }
        item {
            Spacer(modifier = Modifier.width(200.dp))
        }
    }
}