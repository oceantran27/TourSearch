package com.example.flybooking.ui.screens.home.flights

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
import com.example.flybooking.ui.viewmodel.FlightUiState
import com.example.flybooking.ui.viewmodel.FlightViewModel

@Composable
fun FlightsEditScreen(
    modifier: Modifier = Modifier,
    flightViewModel: FlightViewModel
) {
    val flightUiState = flightViewModel.flightUiState as FlightUiState.Success
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(flightUiState.offers) { offer ->
            SelectableCard(
                selected = offer == flightUiState.selected,
                onClick = { flightViewModel.selectOffer(offer) },
                onLongClick = {}
            ) {
                FlightOfferCard(
                    offer = offer,
                )
            }
        }
    }
}