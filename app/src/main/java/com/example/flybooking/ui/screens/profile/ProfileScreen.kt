package com.example.flybooking.ui.screens.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.activity.MainActivity
import com.example.flybooking.R
import com.example.flybooking.help.convertTimestampToDate
import com.example.flybooking.ui.viewmodel.AuthState
import com.example.flybooking.ui.viewmodel.AuthViewModel
import java.io.File

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    val authState = authViewModel.authState.observeAsState()
    val user by authViewModel.userStateFlow.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.UnAuthenticated -> {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK;
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = if (user?.avatarUri?.toUri() != null) {
                            val file = File(user?.avatarUri?.toUri()!!.path ?: "")
                            if (file.exists() && file.isFile) {
                                rememberImagePainter(user?.avatarUri?.toUri())
                            } else {
                                painterResource(id = R.drawable.men)
                            }
                        } else {
                            painterResource(id = R.drawable.men)
                        },
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user?.fullName ?: "Loading...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4A00E0)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            ProfileItem(icon = Icons.Default.Edit, text = user?.fullName ?: "Loading...")
            ProfileItem(
                icon = painterResource(id = R.drawable.cake),
                text = convertTimestampToDate(user?.birthday ?: 0L)
            )
            ProfileItem(icon = Icons.Default.Phone, text = user?.phoneNumber ?: "Loading...")
            ProfileItem(icon = Icons.Default.Email, text = user?.email ?: "Loading...")
            ProfileItem(
                icon = Icons.Default.Visibility,
                text = "Password",
                showChangeButton = true,
                onChangeClick = {
                    navController.navigate("changePassword")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { navController.navigate("edit") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Edit profile",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.signOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(Color.Red),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Sign out",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileItem(
    icon: Any,
    text: String,
    showChangeButton: Boolean = false,
    onChangeClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        if (icon is Painter) {
            Icon(painter = icon, contentDescription = text, tint = Color(0xFF8E2DE2))
        } else if (icon is ImageVector) {
            Icon(imageVector = icon, contentDescription = text, tint = Color(0xFF8E2DE2))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = text, fontSize = 16.sp, color = Color.Gray)

        if (showChangeButton) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = { onChangeClick?.invoke() })
            ) {
                Text(text = "Change", fontSize = 14.sp, color = Color(0xFF4A00E0))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color(0xFF4A00E0),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
