package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.flights.FlightsEditScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import com.example.flybooking.ui.viewmodel.SharedViewModel

class FlightsEditActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyBookingTheme {
                FlightsEditScreen(
                    flightViewModel = SharedViewModel.flightViewModel!!,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}