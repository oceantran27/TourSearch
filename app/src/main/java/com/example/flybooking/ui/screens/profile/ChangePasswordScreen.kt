package com.example.flybooking.ui.screens.profile

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flybooking.AppViewModelProvider
import com.example.flybooking.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun ChangePasswordScreen(
    authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController
) {
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    val isPasswordValid = newPassword.value == confirmPassword.value
    val coroutineScope = rememberCoroutineScope()

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
                .padding(16.dp)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Change Password",
                fontSize = 24.sp,
                color = Color(0xFF4A00E0),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            PasswordTextField(
                value = currentPassword.value,
                onValueChange = { currentPassword.value = it },
                label = "Current Password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = newPassword.value,
                onValueChange = { newPassword.value = it },
                label = "New Password"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = "Confirm New Password"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isPasswordValid) {
                        coroutineScope.launch {
                            authViewModel.changePassword(
                                currentPassword = currentPassword.value,
                                newPassword = newPassword.value,
                                confirmPassword = confirmPassword.value
                            ).onSuccess {
                                Toast.makeText(
                                    navController.context,
                                    "Password changed successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack() // Navigate back
                            }.onFailure { exception ->
                                Toast.makeText(
                                    navController.context,
                                    exception.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Passwords do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Change Password", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        visualTransformation = PasswordVisualTransformation(),
        isError = value.isEmpty(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF4A00E0),
            unfocusedIndicatorColor = Color.Gray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
