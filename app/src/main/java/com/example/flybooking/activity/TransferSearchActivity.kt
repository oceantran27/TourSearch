package com.example.flybooking.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.flybooking.ui.screens.home.transfer.TransferSearchScreen
import com.example.flybooking.ui.theme.FlyBookingTheme

class TransferSearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlyBookingTheme {
                Scaffold { contentPadding ->
                    TransferSearchScreen(modifier = Modifier.padding(contentPadding))
                }
            }
        }
    }
}