package com.example.flybooking.activity

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flybooking.firebase.FirebaseAuthService
import com.example.flybooking.firebase.FirebaseFirestoreService
import com.example.flybooking.ui.viewmodel.ActivitiesViewModel
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.BookingViewModel
import com.example.flybooking.ui.viewmodel.FlightViewModel
import com.example.flybooking.ui.viewmodel.HomeViewModel
import com.example.flybooking.ui.viewmodel.HotelViewModel
import com.example.flybooking.ui.viewmodel.TransferViewModel

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
            HotelViewModel(
                repository = application().container.repository
            )
        }
        initializer {
            TransferViewModel(
                repository = application().container.repository
            )
        }
        initializer {
            AuthViewModel(
                authService = FirebaseAuthService(),
                firestoreService = FirebaseFirestoreService()
            )
        }
        initializer {
            BookingViewModel(
                firestoreService = FirebaseFirestoreService()
            )
        }
    }
}

fun CreationExtras.application(): FlightBookingApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightBookingApplication)