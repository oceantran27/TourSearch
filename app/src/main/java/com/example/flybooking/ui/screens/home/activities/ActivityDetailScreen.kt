package com.example.flybooking.ui.screens.home.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flybooking.model.response.amadeus.Activity

@Composable
fun ActivityDetailScreen(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            val bannerImageRequest = ImageRequest.Builder(context)
                .data(activity.pictures!!.first())
                .crossfade(true)
                .build()
            val bannerHeight = 330.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(bannerHeight)
            ) {
                AsyncImage(
                    model = bannerImageRequest,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(bannerHeight)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = activity.name!!,
                        color = Color.White,
                        fontSize = 30.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = activity.description!!
                    .replace("<br>", "\n")
                    .replace("<br />", "\n"),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(activity.pictures!!) { imageUrl ->
            val imageRequest = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build()

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { (context as? android.app.Activity)?.finish() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.4f), shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}