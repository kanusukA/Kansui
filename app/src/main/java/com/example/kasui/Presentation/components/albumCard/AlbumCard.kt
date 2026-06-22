package com.example.kasui.Presentation.components.albumCard


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kasui.R
import com.example.kasui.ui.UncutSans

@Composable
fun AlbumCard(
//    albumName: String,
//    artistName: String,
//    image: Int

){
    Box (
        modifier = Modifier
            .requiredSize(170.dp)
    ){
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .requiredSize(160.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(24.dp))
                .clip(shape = RoundedCornerShape(size = 24.dp))
        ) {
            Image(painterResource(R.drawable.cover),
                contentScale = ContentScale.FillBounds,
                contentDescription = null)

        }
        Column (
            modifier = Modifier.align(Alignment.BottomStart)
        ){
            Text(
                text = "Long Nights and Wasted Affairs",
                fontFamily = UncutSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)

            )
            Text(text = "Mind's Eye",
                fontFamily = UncutSans,
                fontWeight = FontWeight.Light,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.75f)
                )
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}


@Preview
@Composable
fun previewAlbumCard(){
    AlbumCard()
}