package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.search.FlightHotelActivityScreen
import com.example.flybooking.ui.theme.FlyBookingTheme

class SearchFlightHotelAndActivitiesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    FlightHotelActivityScreen(
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}
