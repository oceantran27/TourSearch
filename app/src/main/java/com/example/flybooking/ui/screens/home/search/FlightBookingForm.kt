package com.example.flybooking.ui.screens.home.search

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.activity.ResultActivity
import com.example.flybooking.model.City
import com.example.flybooking.ui.viewmodel.HomeViewModel
import com.example.flybooking.ui.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FlightBookingForm(
    homeViewModel: HomeViewModel,
    //prePickedDeparture: City = City("", "", ""),
    //prePickedDestination: City = City("", "", ""),
    modifier: Modifier = Modifier
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ô nhập thông tin chuyến bay đi và đến
        DepartureAndDestinationInput(
            onDepartureSelected = homeViewModel::updateDeparture,
            onDestinationSelected = homeViewModel::updateDestination,
            onDepartureInvalidOption = {
                homeViewModel.updateDeparture(City("", "", ""))
            },
            onDestinationInvalidOption = {
                homeViewModel.updateDestination(City("", "", ""))
            },
            //prePickedDeparture = prePickedDeparture,
            //prePickedDestination = prePickedDestination
        )

        // Ô nhập cho số lượng hành khách
        PassengerNumberInput(
            adultCount = homeUiState.passengerAdultCount,
            childCount = homeUiState.passengerChildCount,
            onAddAdult = homeViewModel::increaseAdultCount,
            onAddChild = homeViewModel::increaseChildCount,
            onRemoveAdult = homeViewModel::decreaseAdultCount,
            onRemoveChild = homeViewModel::decreaseChildCount
        )

        // Ô nhập cho ngày đi và ngày về
        DateInputs(
            departureDate = homeUiState.departureDate,
            returnDate = homeUiState.returnDate,
            onDepartureDateChange = homeViewModel::updateDepartureDate,
            onReturnDateChange = homeViewModel::updateReturnDate
        )

        // Ô nhập số tiền
        MoneyInput(
            onMoneyUpdate = homeViewModel::updateMoney,
        )

        // Nút tìm kiếm
        SubmitButton(
            onClick = {
                SharedViewModel.set(
                    departure = homeUiState.departure,
                    destination = homeUiState.destination,
                    departureDate = formatDate(homeUiState.departureDate),
                    returnDate = formatDate(homeUiState.returnDate),
                    adults = homeUiState.passengerAdultCount,
                    children = homeUiState.passengerChildCount,
                    budget = homeUiState.moneyAmount.toInt()
                )
                //val jsonString = Json.encodeToString(SearchInputData.serializer(), searchData)
                val intent = Intent(context, ResultActivity::class.java).apply {
                    //putExtra("search_input_data", jsonString)
                }
                context.startActivity(intent)
            },
            enabled = homeUiState.isFormValid
        )
    }
}

@Composable
fun LabeledField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        content()
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}