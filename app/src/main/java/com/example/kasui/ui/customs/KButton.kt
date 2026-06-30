package com.example.kasui.ui.customs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasui.ui.ViaodaLibre
import com.example.kasui.ui.surfaceColor
import com.example.kasui.ui.variantHighColor

@Composable
fun KButton(title: String, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .widthIn(min = 100.dp)
            .height(48.dp)
            .clip(CircleShape)
            .background(color = variantHighColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(title, fontSize = 20.sp, fontFamily = ViaodaLibre, color = surfaceColor)
    }
}