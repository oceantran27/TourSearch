package com.example.flybooking.navigation

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flybooking.navigation.bottombar.CustomNavigationBar

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(AppScreens.Home) }

    Scaffold(
        bottomBar = {
            CustomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { screen ->
                    selectedTab = screen
                    navController.navigate(screen.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
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
            composable(AppScreens.Home.name) {
                ScreenPlaceholder(screenName = AppScreens.Home.name)
            }
            composable(AppScreens.Explore.name) {
                ScreenPlaceholder(screenName = AppScreens.Explore.name)
            }
            composable(AppScreens.Bookmarks.name) {
                ScreenPlaceholder(screenName = AppScreens.Bookmarks.name)
            }
            composable(AppScreens.Profile.name) {
                ScreenPlaceholder(screenName = AppScreens.Profile.name)
            }
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
