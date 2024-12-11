package com.example.flybooking.ui.screens.home.input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flybooking.R

@Composable
fun PassengerInfo(
    label: String,
    addPassenger: () -> Unit,
    removePassenger: () -> Unit,
    txtDesc: String,
    modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.passenger_ic),
            contentDescription = label,
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "-",
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = removePassenger)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp, modifier = Modifier.semantics {
                contentDescription = txtDesc
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "+",
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = addPassenger)
            )
        }
    }
}

@Composable
fun PassengerNumberInput(
    adultCount: Int,
    childCount: Int,
    onAddAdult: () -> Unit,
    onRemoveAdult: () -> Unit,
    onAddChild: () -> Unit,
    onRemoveChild: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        LabeledField(label = "Passengers") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val noOfAdults = adultCount.toString() + " Adult"
                val noOfChildren = childCount.toString() + " Child"
                PassengerInfo(
                    label = noOfAdults,
                    addPassenger = onAddAdult,
                    removePassenger = onRemoveAdult,
                    txtDesc = "Adults text",
                    modifier = Modifier.weight(1f).semantics {
                        contentDescription = "Adults"
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                PassengerInfo(
                    label = noOfChildren,
                    addPassenger = onAddChild,
                    removePassenger = onRemoveChild,
                    txtDesc = "Children text",
                    modifier = Modifier.weight(1f).semantics {
                        contentDescription = "Children"
                    }
                )
            }
        }
    }
}