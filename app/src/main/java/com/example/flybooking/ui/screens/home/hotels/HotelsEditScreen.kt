package com.example.flybooking.ui.screens.home.hotels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flybooking.ui.screens.others.SelectableCard
import com.example.flybooking.ui.viewmodel.HotelUiState
import com.example.flybooking.ui.viewmodel.HotelViewModel

@Composable
fun HotelsEditScreen(
    modifier: Modifier = Modifier,
    hotelViewModel: HotelViewModel
) {
    val hotelUiState = hotelViewModel.hotelUiState as HotelUiState.Success
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hotelUiState.hotelList) { offer ->
            SelectableCard(
                selected = offer == hotelUiState.selected,
                onClick = { hotelViewModel.selectHotel(offer) },
                onLongClick = {},
                height = 450.dp
            ) {
                HotelCard(hotel = offer, enableAmenities = false)
            }
        }
    }
}