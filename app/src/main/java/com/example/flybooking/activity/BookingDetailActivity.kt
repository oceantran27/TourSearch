package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.result.ResultScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import com.example.flybooking.ui.viewmodel.SharedViewModel

class BookingDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlyBookingTheme {
                Scaffold { paddingValues ->
                    val booking = SharedViewModel.booking
                    ResultScreen(
                        modifier = Modifier.padding(paddingValues),
                        activities = booking?.activities,
                        hotel = booking?.hotels?.firstOrNull(),
                        flight = booking?.flights?.firstOrNull(),
                        transfers = booking?.transfers,
                        showSaveButton = false
                    )
                }
            }
        }
    }
}