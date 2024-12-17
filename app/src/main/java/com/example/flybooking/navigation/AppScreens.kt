package com.example.flybooking.navigation

enum class AppScreens {
    Home,

    //    SearchResults,
//    Explore,
    Bookmark,
    Settings
}

fun onNavBar(screen: AppScreens): Boolean {
    return screen == AppScreens.Home ||
            screen == AppScreens.Bookmark ||
            screen == AppScreens.Settings
}