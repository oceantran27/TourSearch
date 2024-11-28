package com.example.flybooking

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.FlightViewModel
import com.example.flybooking.ui.viewmodel.HomeViewModel
import com.example.flybooking.ui.viewmodel.HotelsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                repository = application().container.repository
            )
        }
        initializer {
            ActivitiesViewModel(
                repository = application().container.repository
            )
        }
        initializer {
            FlightViewModel(
                repository = application().container.repository
            )
        }
        initializer {
            HotelsViewModel(
                repository = application().container.repository
            )
        }

        initializer {
            AuthViewModel()
        }
    }
}

fun CreationExtras.application(): FlightBookingApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightBookingApplication)