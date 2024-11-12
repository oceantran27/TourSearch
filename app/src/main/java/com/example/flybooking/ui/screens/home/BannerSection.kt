package com.example.flybooking.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.R
import com.example.flybooking.ui.theme.ButtonBackground

@Composable
fun HeaderSection(
    painter: Painter,
    name: String,
    desc: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStartPercent = 20, bottomEndPercent = 20))
            .background(ButtonBackground),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.world),
            contentDescription = "Banner_Image",
            contentScale = ContentScale.FillHeight,
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 32.dp, bottom = 32.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            UserInfo(
                painter = painter,
                name = name
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(desc, color = Color.White, fontSize = 24.sp)
        }
    }
}