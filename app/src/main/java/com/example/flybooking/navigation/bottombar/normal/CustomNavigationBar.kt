package com.example.flybooking.navigation.bottombar.normal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flybooking.navigation.AppScreens

@Composable
fun CustomNavigationBar(
    selectedTab: AppScreens,
    onTabSelected: (AppScreens) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppScreens.entries.forEach { screen ->
            CustomNavigationBarItem(
                screen = screen,
                isSelected = screen == selectedTab,
                onClick = { onTabSelected(screen) }
            )
        }
    }
}