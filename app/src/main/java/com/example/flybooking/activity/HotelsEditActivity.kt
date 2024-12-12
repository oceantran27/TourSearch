package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.hotels.HotelsEditScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import com.example.flybooking.ui.viewmodel.SharedViewModel

class HotelsEditActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyBookingTheme {
                HotelsEditScreen(
                    hotelViewModel = SharedViewModel.hotelViewModel!!,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}