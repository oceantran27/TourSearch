package com.example.flybooking.ui.screens.home.transfer

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.model.TransferInfo
import com.example.flybooking.model.TransferLocInfo
import com.example.flybooking.model.response.amadeus.Converted
import com.example.flybooking.model.response.amadeus.GeoCode
import com.example.flybooking.model.response.amadeus.Seat
import com.example.flybooking.model.response.amadeus.ServiceProvider
import com.example.flybooking.model.response.amadeus.Transfer
import com.example.flybooking.model.response.amadeus.Vehicle
import com.example.flybooking.ui.screens.home.hotels.hotelMock
import com.example.flybooking.ui.screens.home.result.LoadingScene
import com.example.flybooking.ui.viewmodel.ActivitiesUiState
import com.example.flybooking.ui.viewmodel.SharedViewModel
import com.example.flybooking.ui.viewmodel.TransferUiState
import com.example.flybooking.ui.viewmodel.TransferViewModel

@Composable
fun TransferSearchScreen(
    modifier: Modifier = Modifier,
    transferViewModel: TransferViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    //val hotel = (SharedViewModel.hotelViewModel?.hotelUiState as? HotelUiState.Success)?.selected?.toTransferLoc()!!
    val hotel = hotelMock.toTransferLoc()
    val activities = (SharedViewModel.activitiesViewModel?.activitiesUiState as? ActivitiesUiState.Success)?.selected?.map { it.toTransferLoc() }!!
    val inputs = ArrayList<TransferInfo>()
    activities.forEach { activity ->
        inputs.add(TransferInfo(hotel, activity, "2024-12-10T10:29:45"))
    }

    val transferUiState = transferViewModel.transferUiState

    val isLoading = transferUiState is TransferUiState.Loading
    val isError = transferUiState is TransferUiState.Error

    LaunchedEffect(Unit) {
        val countryCode = SharedViewModel.destination!!.countryCode
        transferViewModel.searchTransfers(inputs, countryCode)
    }

    if (isError) {
        Text("Error")
    } else if (isLoading) {
        LoadingScene()
    } else {
        val transfers = (transferViewModel.transferUiState as TransferUiState.Success).transfers
        LazyColumn(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transfers) { transferObject ->
                TransferDisplay(
                    transfer = transferObject.result,
                    info = transferObject.info
                )
            }
        }
    }
}

@Composable
fun TransferDisplay(
    modifier: Modifier = Modifier,
    transfer: Transfer,
    info: TransferInfo
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth().animateContentSize()
    ) {
        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = "Move from ${info.from.addressLine} to ${info.to.addressLine}",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Selected",
                    modifier = Modifier.weight(0.1f).clickable(onClick = { expanded = !expanded })
                )
            }
        }
        if (expanded) {
            TransferCard(transfer = transfer)
        }
    }
}

@Preview
@Composable
fun TransferDisplayPreview() {
    TransferDisplay(
        transfer = Transfer(
            id = "4997849412",
            transferType = "PRIVATE",
            vehicle = Vehicle(
                "Luxury rides with maximum comfort",
                "https://img.mydriver.com/image/upload/v1550760370/cms_images/g3sgdn0iczw0vnwlny6o.png",
                listOf(Seat(4))
            ),
            serviceProvider = ServiceProvider(
                "SMD",
                "Sixt Ride",
                "https://img.mydriver.com/image/upload/v1549011723/logos/ride/Sixt_ride_2d_4c_pos.png"
            ),
            converted = Converted(
                "100",
                "EUR"
            ),
            methodsOfPaymentAccepted = listOf("INVOICE")
        ),
        info = TransferInfo(
            TransferLocInfo(
                "Hotel A",
                GeoCode(
                    48.859466,
                    2.2976965
                )
            ),
            TransferLocInfo(
                "Hotel B",
                GeoCode(
                    48.859466,
                    2.2976965
                )
            ),
            "2024-12-10T10:29:45"
        )
    )
}