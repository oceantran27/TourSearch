package com.example.flybooking.ui.screens.activities

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flybooking.ActivityDetailActivity
import com.example.flybooking.model.response.Activity
import com.example.flybooking.ui.theme.ButtonBackground
import kotlinx.serialization.json.Json

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreviewActivityCard(
    onClick: () -> Unit,
    selected: Boolean,
    activity: Activity
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(activity.pictures!!.first())
        .crossfade(true)
        .build()
    val targetScale = if (selected) 0.95f else 1f
    val scale by animateFloatAsState(targetValue = targetScale)

    Card(
        modifier = Modifier
            .fillMaxWidth(scale)
            .height(200.dp * scale)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    val jsonActivity = Json.encodeToString(Activity.serializer(), activity)
                    val intent = Intent(context, ActivityDetailActivity::class.java).apply {
                        putExtra("activity_data", jsonActivity)
                    }
                    context.startActivity(intent)
                }
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = activity.name!!,
                    color = Color.White
                )
                Text(
                    text = activity.price.toString(),
                    color = Color.White
                )
            }
            if (selected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.4f))
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(ButtonBackground.copy(alpha = 0.7f), shape = CircleShape)
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}