package com.example.flybooking.ui.screens.others

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flybooking.ui.theme.ButtonBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableCard(
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val targetScale = if (selected) 0.95f else 1f
    val scale by animateFloatAsState(targetValue = targetScale, label = "")
    Card(
        modifier = Modifier
            .fillMaxWidth(scale)
            .height(200.dp * scale)
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
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