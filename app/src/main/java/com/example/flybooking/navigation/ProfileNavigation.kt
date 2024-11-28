package com.example.flybooking.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flybooking.AppViewModelProvider
import com.example.flybooking.ui.screens.profile.ChangePasswordScreen
import com.example.flybooking.ui.screens.profile.EditProfileScreen
import com.example.flybooking.ui.screens.profile.ProfileScreen
import com.example.flybooking.ui.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(navController = navController, startDestination = "profile", builder = {
        composable(route = "profile") {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = "edit") {
            EditProfileScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(route = "changePassword") {
            ChangePasswordScreen(navController = navController, authViewModel = authViewModel)
        }
    })
}