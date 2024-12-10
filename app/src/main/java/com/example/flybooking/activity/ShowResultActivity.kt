package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.result.ResultScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.FlightUiState
import com.example.flybooking.ui.viewmodel.HotelUiState
import com.example.flybooking.ui.viewmodel.SharedViewModel
import com.example.flybooking.ui.viewmodel.TransferUiState

class ShowResultActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    ResultScreen(
                        modifier = Modifier.padding(paddingValues),
                        activities = (SharedViewModel.activitiesViewModel?.activitiesUiState as? ActivitiesUiState.Success)?.selected,
                        hotel = (SharedViewModel.hotelViewModel?.hotelUiState as? HotelUiState.Success)?.selected,
                        flight = (SharedViewModel.flightViewModel?.flightUiState as? FlightUiState.Success)?.selected,
                        transfers = (SharedViewModel.transferViewModel?.transferUiState as? TransferUiState.Success)?.transfers
                    )
                }
            }
        }
    }
}