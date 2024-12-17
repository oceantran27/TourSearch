package com.example.flybooking.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.activity.BookingDetailActivity
import com.example.flybooking.model.Booking
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.BookingViewModel
import com.example.flybooking.ui.viewmodel.SharedViewModel

@Composable
fun BookmarkScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    bookingViewModel: BookingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val userState = authViewModel.userStateFlow.collectAsState().value
    val loadingState = remember { mutableStateOf(true) }
    val bookingHistoryState = remember { mutableStateOf<List<Booking>>(emptyList()) }

    if (userState == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Please log in to view your booking history.")
        }
        return
    }

    LaunchedEffect(Unit) {
        loadingState.value = true
        bookingHistoryState.value = bookingViewModel.getAllBooking(userState.bookingHistory)
        loadingState.value = false
    }

    if (loadingState.value) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(bookingHistoryState.value) { booking ->
                BookingCard(booking)
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
    val context: Context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                SharedViewModel.booking = booking
                val intent = Intent(context, BookingDetailActivity::class.java)
                context.startActivity(intent)
            }
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
