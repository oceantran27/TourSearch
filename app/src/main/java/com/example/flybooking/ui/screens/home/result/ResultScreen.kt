package com.example.flybooking.ui.screens.home.result

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.ActivityDetailActivity
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.model.Booking
import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.FlightOffer
import com.example.flybooking.ui.screens.home.activities.PreviewActivityCard
import com.example.flybooking.ui.screens.home.flights.FlightOfferCard
import com.example.flybooking.ui.screens.home.hotels.HotelCard
import com.example.flybooking.ui.screens.home.search.LoadingScene
import com.example.flybooking.ui.screens.home.transfer.TransferDisplay
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.BookingState
import com.example.flybooking.ui.viewmodel.HotelObject
import com.example.flybooking.ui.viewmodel.TransferObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    activities: List<Activity>? = null,
    hotel: HotelObject? = null,
    flight: FlightOffer? = null,
    transfers: List<TransferObject>? = null,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    showSaveButton: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    if (activities == null || hotel == null || flight == null || transfers == null) {
        Text(
            text = "Error",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        return
    }

    val bookingState by authViewModel.addBookingState.observeAsState()

    if (bookingState is BookingState.Loading) {
        LoadingScene()
        return
    } else if (bookingState is BookingState.Error) {
        Text(
            text = "Error",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        return
    } else if (bookingState is BookingState.Success) {
        LaunchedEffect(key1 = bookingState) {
            Toast.makeText(context, "Booking created successfully", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier.padding(16.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
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

            item {
                Spacer(modifier.height(16.dp))
            }
        }

        if (showSaveButton) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        authViewModel.addHistoryBooking(
                            Booking(
                                id = randomizeBookingId(),
                                activities = activities,
                                hotels = listOf(hotel),
                                flights = listOf(flight),
                                transfers = transfers
                            )
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Transparent)
            ) {
                Icon(Icons.Filled.Save, contentDescription = "Save Booking")
            }
        }
    }
}


fun randomizeBookingId(): String {
    val allowedChars = ('A'..'Z') + ('0'..'9')
    return (1..5)
        .map { allowedChars.random() }
        .joinToString("")
}