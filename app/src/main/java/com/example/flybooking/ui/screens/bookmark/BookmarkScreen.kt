package com.example.flybooking.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.flybooking.model.Booking
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.BookingState
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.ui.viewmodel.BookingViewModel

@Composable
fun BookmarkScreen(
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    bookingViewModel: BookingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val userState = authViewModel.userStateFlow.collectAsState().value
    val addBookingState by authViewModel.addBookingState.observeAsState(BookingState.Loading)

    if (userState == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Please log in to view your booking history.")
        }
        return
    }

    val bookingHistory = userState.bookingHistory

    if (addBookingState is BookingState.Loading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(bookingHistory) { bookingId ->
            BookingCard(bookingId = bookingId, bookingViewModel = bookingViewModel)
        }
    }
}

@Composable
fun BookingCard(bookingId: String, bookingViewModel: BookingViewModel) {
    val bookingState = bookingViewModel.bookingState.observeAsState(BookingState.Loading)

    LaunchedEffect(bookingId) {
        bookingViewModel.dbReadBooking(bookingId)
    }

    when (val state = bookingState.value) {
        is BookingState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is BookingState.BookingDetails -> {
            val booking = state.booking
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Booking ID: ${booking.id}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Activities: ${booking.activities.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hotels: ${booking.hotels.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Flights: ${booking.flights.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Transfers: ${booking.transfers.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        is BookingState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Error fetching booking details: ${state.message}")
            }
        }

        else -> {
        }
    }
}

