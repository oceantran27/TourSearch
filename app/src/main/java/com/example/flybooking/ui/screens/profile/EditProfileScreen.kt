package com.example.flybooking.ui.screens.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flybooking.activity.AppViewModelProvider
import com.example.flybooking.R
import com.example.flybooking.help.convertTimestampToDate
import com.example.flybooking.util2.DatePickerModal
import com.example.flybooking.ui.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    val user by authViewModel.userStateFlow.collectAsState()
    var name by remember { mutableStateOf(user?.fullName ?: "Loading") }
    var phone by remember { mutableStateOf(user?.phoneNumber ?: "Loading") }
    var email by remember { mutableStateOf(user?.email ?: "Loading") }
    var avatarUri by
    remember { mutableStateOf<Uri?>(user?.avatarUri?.toUri()) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
            avatarUri = uri
        }

    var birthdayTimestamp by remember { mutableStateOf(user?.birthday ?: 0L) }
    var birthday by remember { mutableStateOf("") }

    if (birthdayTimestamp != 0L) {
        birthday = convertTimestampToDate(birthdayTimestamp)
    }
    val showDatePicker = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
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
                .padding(
                    top = paddingValues.calculateTopPadding(),
                )
                .verticalScroll(rememberScrollState())
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
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = "Remote Image",
                        placeholder = painterResource(R.drawable.men),
                        error = painterResource(R.drawable.men),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .clickable { launcher.launch("image/*") }
                    )
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Edit Picture",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.BottomEnd)
                            .offset(x = (-8).dp, y = 4.dp)
                            .background(Color(0x80000000), shape = CircleShape)
                            .padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4A00E0)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            EditableProfileItem(label = "Name", value = name) { name = it }
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Birthday",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF8E2DE2)
                )
                OutlinedTextField(
                    value = birthday,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker.value = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                )
            }
            EditableProfileItem(
                label = "Phone",
                value = phone,
                keyboardType = KeyboardType.Phone
            ) { phone = it }
            EditableProfileItem(
                label = "Email",
                value = email,
                keyboardType = KeyboardType.Email
            ) { email = it }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val currentUser = user
                    if (currentUser != null) {
                        val updatedUser = currentUser.copy(
                            fullName = name,
                            email = email,
                            phoneNumber = phone,
                            birthday = birthdayTimestamp,
                            avatarUri = avatarUri.toString(),
                        )
                        if (updatedUser != currentUser)
                            authViewModel.updateProfile(updatedUser)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Save Changes",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showDatePicker.value) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                if (selectedDate != null) {
                    birthday = convertTimestampToDate(selectedDate)
                    birthdayTimestamp = selectedDate
                }
                showDatePicker.value = false
            },
            onDismiss = { showDatePicker.value = false }
        )
    }
}

@Composable
fun EditableProfileItem(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF8E2DE2)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
        )
    }
}

