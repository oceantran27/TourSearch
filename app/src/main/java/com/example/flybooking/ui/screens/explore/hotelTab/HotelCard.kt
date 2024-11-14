package com.example.flybooking.ui.screens.explore.hotelTab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flybooking.R
import com.example.flybooking.model.response.Hotel

@Composable
fun HotelCard(hotel: Hotel) {
    Card(
        modifier = Modifier.padding(8.dp),
//        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Placeholder image for hotel
            Image(
                painter = painterResource(id = R.drawable.men),
                contentDescription = "Hotel Image",
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = hotel.name, style = MaterialTheme.typography.headlineLarge)
            Text(text = "${hotel.address.countryCode} - ${hotel.distance.value} ${hotel.distance.unit}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}