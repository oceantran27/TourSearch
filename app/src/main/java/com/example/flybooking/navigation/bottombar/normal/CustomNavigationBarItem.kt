package com.example.flybooking.navigation.bottombar.normal

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.flybooking.navigation.AppScreens

@Composable
fun CustomNavigationBarItem(
    screen: AppScreens,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize: Dp by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 20.dp,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 100f
        )
    )
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0080FF) else Color.Transparent,
        animationSpec = tween(durationMillis = 200)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 200)
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Gray,
        animationSpec = tween(durationMillis = 200)
    )

    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                color = backgroundColor,
                shape = if (isSelected) RoundedCornerShape(50.dp) else CircleShape
            )
            .padding(horizontal = if (isSelected) 12.dp else 0.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (screen) {
                AppScreens.Home -> Icons.Filled.Home
                AppScreens.Bookmark -> Icons.Filled.Favorite
                AppScreens.Settings -> Icons.Filled.Settings
                // Dummy
            },
            contentDescription = screen.name,
            modifier = Modifier.size(iconSize),
            tint = iconColor
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = screen.name,
                color = labelColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

