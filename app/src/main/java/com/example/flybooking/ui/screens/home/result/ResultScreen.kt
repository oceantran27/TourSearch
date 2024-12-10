package com.example.flybooking.ui.screens.home.result

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.activity.ActivityDetailActivity
import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.FlightOffer
import com.example.flybooking.ui.screens.home.activities.PreviewActivityCard
import com.example.flybooking.ui.screens.home.flights.FlightOfferCard
import com.example.flybooking.ui.screens.home.hotels.HotelCard
import com.example.flybooking.ui.screens.home.transfer.TransferDisplay
import com.example.flybooking.ui.viewmodel.HotelObject
import com.example.flybooking.ui.viewmodel.TransferObject
import kotlinx.serialization.json.Json

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    activities: List<Activity>? = null,
    hotel: HotelObject? = null,
    flight: FlightOffer? = null,
    transfers: List<TransferObject>? = null
) {
    if (activities == null || hotel == null || flight == null || transfers == null) {
        Text(
            text = "Error",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        return
    }

    val context = LocalContext.current

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ----------------- Flight ----------------- //
        item {
            Text(
                text = "Flight",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            FlightOfferCard(offer = flight)
        }

        // ----------------- Hotel ----------------- //
        item {
            Text(
                text = "Hotel",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            HotelCard(hotel = hotel)
        }

        // ----------------- Activities ----------------- //
        item {
            Text(
                text = "Activities",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(activities) { activity ->
            PreviewActivityCard(
                activity = activity,
                onClick = {
                    val jsonActivity = Json.encodeToString(Activity.serializer(), activity)
                    val intent = Intent(context, ActivityDetailActivity::class.java).apply {
                        putExtra("activity_data", jsonActivity)
                    }
                    context.startActivity(intent)
                }
            )
        }

        // ----------------- Transfers ----------------- //
        item {
            Text("Transfers")
        }
        items(transfers) { transferObject ->
            TransferDisplay(
                transfer = transferObject.result,
                info = transferObject.info
            )
        }
    }
}