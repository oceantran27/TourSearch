package com.example.flybooking.ui.screens.home.flights

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.model.SearchInputData
import com.example.flybooking.ui.screens.others.LoadingAnimation
import com.example.flybooking.ui.viewmodel.FlightUiState
import com.example.flybooking.ui.viewmodel.FlightViewModel
import kotlinx.coroutines.launch

@Composable
fun FlightSearchScreen(
    modifier: Modifier = Modifier,
    flightViewModel: FlightViewModel = viewModel(factory = AppViewModelProvider.Factory),
    inputData: SearchInputData
) {
    val flightUiState = flightViewModel.flightUiState
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (flightUiState) {
            is FlightUiState.Loading -> {
                LoadingScene(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is FlightUiState.Success -> {
                SuccessScreen(flightUiState)
            }
            is FlightUiState.Error -> {
                Text(text = "Error loading flights")
            }
        }
//        if (flightUiState is FlightUiState.Loading) {
//            LoadingAnimation(
//                modifier = Modifier.align(Alignment.Center),
//                circleSize = 12.dp,
//                spaceBetween = 20.dp,
//                travelDistance = 15.dp
//            )
//        } else {
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
//            ) {
//                items(flightUiState.value.offers) { offer ->
//                    FlightOfferCard(offer)
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
//        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            flightViewModel.searchFlights(
                departure = inputData.departure,
                destination = inputData.destination,
                departureDate = inputData.departureDate,
                returnDate = inputData.returnDate,
                adults = inputData.adults,
                children = inputData.children
            )
        }
    }
}

@Composable
fun LoadingScene(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation(
            circleSize = 12.dp,
            spaceBetween = 20.dp,
            travelDistance = 15.dp
        )
    }
}

@Composable
fun SuccessScreen(
    successState: FlightUiState.Success,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        items(successState.offers) { offer ->
            FlightOfferCard(
                offer = offer
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}