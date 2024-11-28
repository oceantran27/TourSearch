package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flybooking.ui.screens.home.activities.ActivitiesEditScreen
import com.example.flybooking.ui.theme.FlyBookingTheme
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.SharedViewModel

class ActivitiesEditActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlyBookingTheme {
                ActivitiesEditScreen(
                    activities = (SharedViewModel
                        .activitiesViewModel
                        ?.activitiesUiState as ActivitiesUiState.Success).cards,
                    activitiesViewModel = SharedViewModel.activitiesViewModel!!
                )
            }
        }
    }
}