package com.example.flybooking.ui.screens.home.hotels

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.flybooking.model.response.amadeus.GeoCode
import com.example.flybooking.model.response.tripadvisor.AddressObject
import com.example.flybooking.model.response.tripadvisor.DetailsResponse
import com.example.flybooking.model.response.tripadvisor.Image
import com.example.flybooking.model.response.tripadvisor.Large
import com.example.flybooking.model.response.tripadvisor.Photo
import com.example.flybooking.ui.theme.ButtonBackground
import com.example.flybooking.ui.viewmodel.HotelObject

@Composable
fun HotelCard(
    modifier: Modifier = Modifier,
    hotel: HotelObject
) {
    val pagerState = rememberPagerState(pageCount = { hotel.photos.size })
    val context = LocalContext.current
    var expanded by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF8F8F8))
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) { page ->
            val photo = hotel.photos[page]
            val url = photo.images.large.url
            Box(
                modifier = Modifier.fillMaxWidth().height(225.dp) // Đảm bảo bao bọc cả hình ảnh
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth().height(225.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${page + 1} / ${hotel.photos.size}",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
        Text(
            text = hotel.details!!.name,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        Text(
            text = hotel.details.addr.toString(),
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(hotel.details.ratingImageUrl)
                    .crossfade(true)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.width(50.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text =  "(" + hotel.details.rating + ")",
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = hotel.details.addr.toString(),
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = hotel.details.numReviews + " reviews",
            fontSize = 12.sp,
            color = ButtonBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth().animateContentSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = !expanded }).padding(vertical = 8.dp),
            ) {
                Text(
                    text = "Amenities",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            if (expanded) {
                hotel.details.amenities?.forEach{ amenity ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = amenity,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HotelCardPreview() {
    HotelCard(
        hotel = hotelMock
    )
}

val hotelMock = HotelObject(
    cityName = "Paris",
    cityGeoCode = GeoCode(48.85341,2.3488),
    details = DetailsResponse(
        name = "Hotel France Louvre",
        rating = "3.0",
        ratingImageUrl = "https://www.tripadvisor.com/img/cdsi/img2/ratings/traveler/3.0-66827-5.svg",
        numReviews = 100.toString(),
        priceLevel = "$$$",
        amenities = listOf("Free Wi-Fi", "Free Parking"),
        addr = AddressObject(
            street1 = "123 Main",
            city = "Paris",
            country = "France"
        ),
        latitude = 48.85657.toString(),
        longitude = 2.356067.toString()
    ),
    photos = listOf(
        Photo(Image(Large("https://media-cdn.tripadvisor.com/media/oyster/2600/15/3d/7e/55/superior-room-v18597095.jpg"))),
        Photo(Image(Large("https://media-cdn.tripadvisor.com/media/photo-o/06/bb/0d/39/france-louvre.jpg")))
    )
)