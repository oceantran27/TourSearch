package com.example.flybooking.ui.screens.explore.hotelTab

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.AppViewModelProvider
import com.example.flybooking.model.response.Hotel
import com.example.flybooking.ui.screens.activities.LoadingScene
import com.example.flybooking.ui.viewmodel.HotelsUiState
import com.example.flybooking.ui.viewmodel.HotelsViewModel
import androidx.compose.ui.Modifier // Import chính xác cho Modifier

@Composable
fun HotelsScreen(
    cityCode: String,
    hotelsViewModel: HotelsViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val hotelsUiState by hotelsViewModel.hotelsUiState.collectAsState()

    when (hotelsUiState) {
        is HotelsUiState.Loading -> LoadingScene()
        is HotelsUiState.Error -> ErrorScene()
        is HotelsUiState.Success -> {
            val hotels = (hotelsUiState as HotelsUiState.Success).hotels
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(hotels) { hotel ->
                    HotelCard(hotel = hotel)
                }
            }
        }
    }
}


@Composable
fun ErrorScene() {
    // Thành phần hiển thị lỗi, ví dụ:
    androidx.compose.material3.Text(text = "Error loading hotels")
}
