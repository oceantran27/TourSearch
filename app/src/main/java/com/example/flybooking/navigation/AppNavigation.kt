package com.example.flybooking.navigation

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flybooking.navigation.bottombar.droplet.AnimatedBottomBar
import com.example.flybooking.ui.screens.explore.ExploreScreen
import com.example.flybooking.ui.screens.home.HomeScreen
import com.example.flybooking.ui.screens.home.search.HomeScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var selectedTab by rememberSaveable { mutableStateOf(AppScreens.Home) }

    Log.d("CurrentScreen", "Current screen: ${selectedTab.name}")

    fun navigateTo(screen: AppScreens, name: String = "") {
        Log.d("navigateTo", "Navigate to $name")
        selectedTab = screen
        navController.navigate(name) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        bottomBar = {
//            CustomNavigationBar(
//                onTabSelected = { screen -> navigateTo(screen, screen.name) },
//                selectedTab = selectedTab,
//                modifier = Modifier.navigationBarsPadding()
//            )
            AnimatedBottomBar(
                selectedTab = selectedTab.ordinal,
                onTabSelected = {
                    selectedTab = AppScreens.entries.toTypedArray()[it]
                    navigateTo(selectedTab, selectedTab.name)
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Home.name,
            modifier = modifier.padding(innerPadding),
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            composable(
                route = "${AppScreens.Home.name}?destination={destination}",
                arguments = listOf(
                    navArgument(name = "destination") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                HomeScreen(
                    destination = backStackEntry.arguments?.getString("destination") ?: "",
                    modifier = Modifier.semantics { contentDescription = "HomeScreen" },
                    onSearchClick = {
//                        navigateTo(AppScreens.SearchResults, AppScreens.SearchResults.name)
                    }
                )
            }
            composable(AppScreens.Explore.name) {
//                ScreenPlaceholder(screenName = AppScreens.Explore.name)
                ExploreScreen()
            }
            composable(AppScreens.Bookmarks.name) {
                ScreenPlaceholder(screenName = AppScreens.Bookmarks.name)
            }
            composable(AppScreens.Profile.name) {
                ScreenPlaceholder(screenName = AppScreens.Profile.name)
            }
//            composable(AppScreens.SearchResults.name) {
//                ScreenPlaceholder(screenName = AppScreens.SearchResults.name)
//            }
        }
    }
}

@Composable
fun ScreenPlaceholder(screenName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Screen_$screenName" },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "This is the $screenName")
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}
