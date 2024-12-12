package com.example.flybooking.ui.screens.home.search

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.ActivitiesEditActivity
import com.example.flybooking.activity.ActivityDetailActivity
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.activity.FlightsEditActivity
import com.example.flybooking.activity.HotelsEditActivity
import com.example.flybooking.activity.TransferSearchActivity
import com.example.flybooking.model.response.amadeus.Activity
import com.example.flybooking.model.response.amadeus.ActivityCard
import com.example.flybooking.model.response.amadeus.FlightOffer
import com.example.flybooking.ui.screens.home.activities.PreviewActivityCard
import com.example.flybooking.ui.screens.home.flights.FlightOfferCard
import com.example.flybooking.ui.screens.home.hotels.HotelCard
import com.example.flybooking.ui.screens.others.LoadingAnimation
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import com.example.flybooking.ui.viewmodel.FlightUiState
import com.example.flybooking.ui.viewmodel.FlightViewModel
import com.example.flybooking.ui.viewmodel.HotelUiState
import com.example.flybooking.ui.viewmodel.HotelViewModel
import com.example.flybooking.ui.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@Composable
fun FlightHotelActivityScreen(
    modifier: Modifier = Modifier,
    activitiesViewModel: ActivitiesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    flightViewModel: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory),
    hotelViewModel: HotelViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val activitiesUiState = activitiesViewModel.activitiesUiState
    val flightsUiState = flightViewModel.flightUiState
    val hotelUiState = hotelViewModel.hotelUiState
    val context = LocalContext.current

    val isLoading = (
            activitiesUiState is ActivitiesUiState.Loading
            || flightsUiState is FlightUiState.Loading
            || hotelUiState is HotelUiState.Loading
           )

    val isError = (
            activitiesUiState is ActivitiesUiState.Error
            || flightsUiState is FlightUiState.Error
            || hotelUiState is HotelUiState.Error
            )

    val activities: List<Activity> = if (activitiesUiState is ActivitiesUiState.Success) {
        activitiesUiState.selected
    } else {
        emptyList()
    }
    val offer: FlightOffer = if (flightsUiState is FlightUiState.Success) {
        flightsUiState.selected ?: FlightOffer.empty()
    } else {
        FlightOffer.empty()
    }

    if (isError) {
        Text(text = "Error")
    } else if (isLoading) {
        LoadingScene()
    } else {
        LazyColumn(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -------------------------- Flights -------------------------- //
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                SectionHeader(
                    title = "Flights",
                    onEdit = {
                        SharedViewModel.flightViewModel = flightViewModel
                        val intent = Intent(context, FlightsEditActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                FlightOfferCard(offer = offer)
            }

            // -------------------------- Activities -------------------------- //
            item {
                SectionHeader(
                    title = "Activities",
                    onEdit = {
                        SharedViewModel.activitiesViewModel = activitiesViewModel
                        val intent = Intent(context, ActivitiesEditActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(activities) { activity ->
                val card = ActivityCard(activity)
                PreviewActivityCard(
                    activity = card.activity,
                    onClick = {
                        val jsonActivity = Json.encodeToString(Activity.serializer(), activity)
                        val intent = Intent(context, ActivityDetailActivity::class.java).apply {
                            putExtra("activity_data", jsonActivity)
                        }
                        context.startActivity(intent)
                    }
                )
            }

            // -------------------------- Hotels -------------------------- //
            item {
                SectionHeader(
                    title = "Hotel",
                    onEdit = {
                        SharedViewModel.hotelViewModel = hotelViewModel
                        val intent = Intent(context, HotelsEditActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                HotelCard(
                    hotel = (hotelUiState as HotelUiState.Success).hotelList.first()
                )
//                HotelCardPreview()
            }

            // -------------------------- Proceed -------------------------- //
            item {
                Button(
                    onClick = {
                        SharedViewModel.hotelViewModel = hotelViewModel
                        SharedViewModel.activitiesViewModel = activitiesViewModel
                        SharedViewModel.flightViewModel = flightViewModel
                        val intent = Intent(context, TransferSearchActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Proceed")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            activitiesViewModel.searchActivities(
                destination = SharedViewModel.destination!!.cityCode
            )
        }
        coroutineScope.launch {
            flightViewModel.searchFlights(
                departure = SharedViewModel.departure!!.cityCode,
                destination = SharedViewModel.destination!!.cityCode,
                departureDate = SharedViewModel.departureDate!!,
                returnDate = SharedViewModel.returnDate!!,
                adults = SharedViewModel.adults,
                children = SharedViewModel.children,
            )
        }
        coroutineScope.launch {
            hotelViewModel.searchHotels(
                destination = SharedViewModel.destination!!
            )
        }
    }
}

@Composable
fun LoadingScene(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(
            circleSize = 12.dp,
            spaceBetween = 20.dp,
            travelDistance = 15.dp,
        )
    }
}


@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    title: String,
    onEdit: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        IconButton(
            onClick = onEdit
        )  {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }
    }
}