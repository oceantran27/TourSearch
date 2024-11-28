package com.example.flybooking.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flybooking.ui.screens.login.LoginScreen
import com.example.flybooking.ui.screens.login.SignUpScreen
import com.example.flybooking.ui.screens.profile.ChangePasswordScreen
import com.example.flybooking.ui.screens.profile.EditProfileScreen
import com.example.flybooking.ui.screens.profile.ProfileScreen
import com.example.flybooking.ui.viewmodel.AuthViewModel

@Composable
fun LoginNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable(route = "login") {
            LoginScreen(navController = navController)
        }
        composable(route = "signup") {
            SignUpScreen(navController = navController)
        }
    })
}