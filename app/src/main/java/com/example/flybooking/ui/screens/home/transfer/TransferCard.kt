package com.example.flybooking.ui.screens.home.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.flybooking.model.response.amadeus.Converted
import com.example.flybooking.model.response.amadeus.Seat
import com.example.flybooking.model.response.amadeus.ServiceProvider
import com.example.flybooking.model.response.amadeus.Transfer
import com.example.flybooking.model.response.amadeus.Vehicle
import com.example.flybooking.ui.theme.ButtonBackground

@Composable
fun TransferCard(
    modifier: Modifier = Modifier,
    transfer: Transfer
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFFF8F8F8))
    ) {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(transfer.serviceProvider.logoUrl)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().height(100.dp).alpha(0.08f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = transfer.vehicle.description,
                        fontWeight = FontWeight.Bold,
                        //textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = ButtonBackground,
                        modifier = Modifier.weight(1f).fillMaxWidth().padding(start = 8.dp)
                    )
                    Card(
                        modifier = Modifier.width(100.dp).height(60.dp).padding(8.dp),
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$${transfer.converted.monetaryAmount}",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.alpha(0.6f)
                            )
                        }
                    }
                }
            }
            VehicleDetail(
                modifier = Modifier.padding(16.dp),
                vehicle = transfer.vehicle,
                payment = transfer.methodsOfPaymentAccepted!!,
                serviceProvider = transfer.serviceProvider
            )
        }
    }
}

@Composable
fun VehicleDetail(
    modifier: Modifier = Modifier,
    vehicle: Vehicle,
    payment: List<String>,
    serviceProvider: ServiceProvider
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(vehicle.imageURL)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier.fillMaxWidth().height(150.dp)
            )
        }
        Text(
            text = "Provider name: " + serviceProvider.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Features",
            fontSize = 18.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.Green
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (vehicle.seats.first().count > 1) "${vehicle.seats.first().count} seats" else "${vehicle.seats.first().count} seat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        payment.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Green
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun TransferCardPreview() {
    TransferCard(
        transfer = Transfer(
            id = "1",
            transferType = "Bus",
            vehicle = Vehicle(
                description = "Private Premium Car",
                imageURL = "https://assets.htxstaging.com/imgs/default/vehicle_set/luxury-car.png",
                seats = listOf(Seat(4))
            ),
            serviceProvider = ServiceProvider(
                code = "1",
                name = "HolidayTaxis",
                logoUrl = "https://assets.htxstaging.com/images/logos/Holiday-Taxis-Logo_htx_logo_signature_white_on_blue.png"
            ),
            converted = Converted(
                monetaryAmount = "10.00",
                currencyCode = "USD"
            ),
            methodsOfPaymentAccepted = listOf("Cash", "Credit Card")
        )
    )
}