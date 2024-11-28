package com.example.flybooking.ui.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flybooking.R
import com.example.flybooking.ui.screens.explore.eventTab.EventTab
import com.example.flybooking.ui.screens.explore.hotelTab.HotelsScreen
import com.example.flybooking.ui.screens.explore.vehicleTab.VehicleTab

@Composable
fun ExploreScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.explore_header_img),
                        contentDescription = "Explore Header",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = "Explore Travel",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp)
                    )
                }

                ExploreTopTabBar(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTabIndex) {
            1 -> HotelsScreen(cityCode = "NYC")
            0 -> EventTab(modifier = Modifier.fillMaxSize().padding(innerPadding))
            2 -> VehicleTab(modifier = Modifier.fillMaxSize().padding(innerPadding))
        }
    }
}

@Composable
fun ExploreTopTabBar(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Hotel", "Event", "Vehicle")

    TabRow(
        selectedTabIndex = selectedTabIndex,
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(text = title) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    ExploreScreen()
}