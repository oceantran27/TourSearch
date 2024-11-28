package com.example.flybooking.ui.screens.flights

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.R
import com.example.flybooking.model.response.AirportInfo
import com.example.flybooking.model.response.FlightOffer
import com.example.flybooking.model.response.Itinerary
import com.example.flybooking.model.response.Price
import com.example.flybooking.model.response.Segment
import com.example.flybooking.ui.viewmodel.convertToUSD
import kotlin.math.ceil

@Composable
fun FlightOfferCard(
    offer: FlightOffer,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFFF0F0F0)),
        shape = RoundedCornerShape(15),
        modifier = modifier.padding(horizontal = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_vna_v),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(35.dp).align(Alignment.CenterHorizontally)
            )
            offer.itineraries.forEach { itinerary ->
                Spacer(modifier = Modifier.height(8.dp))
                FlightPath(
                    itinerary = itinerary,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.dash_line),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color(0xFFfafafa)),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "$${
                        ceil(
                            convertToUSD(
                                price = offer.price.total.toDouble(),
                                currency = offer.price.currency
                            )
                        ).toInt()
                    }",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0c79e9),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
fun FlightPath(
    itinerary: Itinerary,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val fontSizeModifier = 1f - 0.2f * (itinerary.segments.size - 1)
        Text(
            text = itinerary.segments.first().departure.iataCode,
            fontSize = 22.sp * fontSizeModifier,
            color = Color(0xFF212121),
            fontWeight = FontWeight.Bold
        )
        itinerary.segments.forEachIndexed { index, segment ->
            Spacer(modifier = Modifier.width(12.dp / itinerary.segments.size))
            Image(
                painter = painterResource(id = R.drawable.line_airple_blue),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp / itinerary.segments.size))
            Text(
                text = segment.arrival.iataCode,
                fontSize = 22.sp * fontSizeModifier,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightOfferCardPreview() {
    FlightOfferCard(
        offer = FlightOffer(
            id = "1",
            itineraries = arrayListOf(
                Itinerary(
                    duration = "PT13H45M",
                    segments = arrayListOf(
                        Segment(
                            departure = AirportInfo(
                                iataCode = "SFO",
                                at = "2022-12-12T12:00:00"
                            ),
                            arrival = AirportInfo(
                                iataCode = "LAX",
                                at = "2022-12-12T14:00:00"
                            ),
                            carrierCode = "AA",
                            duration = "PT13H45M"
                        ),
                        Segment(
                            departure = AirportInfo(
                                iataCode = "LAX",
                                at = "2022-12-12T16:00:00"
                            ),
                            arrival = AirportInfo(
                                iataCode = "HAN",
                                at = "2022-12-12T18:00:00"
                            ),
                            carrierCode = "AA",
                            duration = "PT2H"
                        )
                    )
                ),
                Itinerary(
                    duration = "PT13H45M",
                    segments = arrayListOf(
                        Segment(
                            departure = AirportInfo(
                                iataCode = "SFO",
                                at = "2022-12-12T12:00:00"
                            ),
                            arrival = AirportInfo(
                                iataCode = "LAX",
                                at = "2022-12-12T14:00:00"
                            ),
                            carrierCode = "AA",
                            duration = "PT13H45M"
                        ),
                        Segment(
                            departure = AirportInfo(
                                iataCode = "LAX",
                                at = "2022-12-12T16:00:00"
                            ),
                            arrival = AirportInfo(
                                iataCode = "HAN",
                                at = "2022-12-12T18:00:00"
                            ),
                            carrierCode = "AA",
                            duration = "PT2H"
                        )
                    )
                )
            ),
            price = Price(
                currency = "USD",
                total = "100",
                base = "90"
            )
        )
    )
}