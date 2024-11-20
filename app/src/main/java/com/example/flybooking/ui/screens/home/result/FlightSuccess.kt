package com.example.flybooking.ui.screens.home.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.flybooking.model.response.FlightOffer
import com.example.flybooking.ui.screens.home.flights.FlightOfferCard

@Composable
fun FlightSuccess(
    modifier: Modifier = Modifier,
    offers: List<FlightOffer>,
    onEdit: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SectionHeader(
            title = "Flights",
            onEdit = onEdit,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
        FlightOfferCard(
            offer = offers[0]
        )
    }
}