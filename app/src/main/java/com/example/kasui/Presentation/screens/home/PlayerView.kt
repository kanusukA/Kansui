package com.example.kasui.Presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

import coil3.compose.AsyncImage
import com.example.kasui.R
import com.example.kasui.ui.surfaceHighColor

@Composable
fun PlayerView(
    modifier: Modifier
) {
    // Pill Space
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 74.dp)
            .requiredHeight(56.dp)
            .fillMaxWidth()
            .background(
                surfaceHighColor.copy(alpha = 0.85f),
                shape = RoundedCornerShape(
                    topStart = 36.dp,
                    bottomStart = 12.dp,
                    topEnd = 36.dp,
                    bottomEnd = 12.dp
                )
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(46.dp)
                .clip(CircleShape)
                .align(Alignment.CenterStart),
            model = R.drawable.cover,
            contentDescription = "Album Cover"

        )
    }
}