package com.example.flybooking.ui.screens.home.input

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.ui.viewmodel.AuthViewModel
import com.example.flybooking.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    //destination: City,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()
    val user by authViewModel.userStateFlow.collectAsState()
    val context = LocalContext.current

//    // Cập nhật `destination` chỉ khi `destination` thay đổi
//    LaunchedEffect(destination) {
//        if (destination.name.isNotEmpty()) {
//            homeViewModel.updateDestination(destination)
//        }
//    }

    // Hiển thị Toast khi có thông báo lỗi
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            homeViewModel.clearErrorMessage()
        }
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderSection(
            painter = painterResource(id = homeUiState.bannerImageId),
            name = "Welcome, ${user?.fullName ?: "Guest"} ",
            desc = homeUiState.bannerDesc
        )
        FlightBookingForm(
            homeViewModel = homeViewModel,
//            prePickedDestination = if (homeUiState.destination.name.isNotEmpty()) {
//                homeUiState.destination
//            } else {
//                destination
//            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}


