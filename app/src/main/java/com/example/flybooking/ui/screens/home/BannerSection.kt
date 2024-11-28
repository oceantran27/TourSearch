package com.example.flybooking.ui.screens.home

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flybooking.AppViewModelProvider
import com.example.flybooking.LoginActivity
import com.example.flybooking.ProfileActivity
import com.example.flybooking.R
import com.example.flybooking.ui.theme.ButtonBackground
import com.example.flybooking.ui.viewmodel.AuthViewModel

@Composable
fun HeaderSection(
    painter: Painter,
    name: String,
    desc: String,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStartPercent = 20, bottomEndPercent = 20))
            .background(ButtonBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.world),
            contentDescription = "Banner_Image",
            contentScale = ContentScale.FillHeight,
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 32.dp)
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                contentAlignment = Alignment.TopEnd
            ) {
                OutlinedButton(
                    onClick = {
                        val intent = if (authViewModel.isLoggedIn()) {
                            Intent(context, ProfileActivity::class.java)
                        } else {
                            Intent(context, LoginActivity::class.java)
                        }
                        context.startActivity(intent)
                    },
                    border = BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        text = if (authViewModel.isLoggedIn()) "Account" else "Login",
                        color = Color.White
                    )
                }
            }
            UserInfo(
                painter = painter,
                name = name
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(desc, color = Color.White, fontSize = 24.sp)
        }
    }
}
