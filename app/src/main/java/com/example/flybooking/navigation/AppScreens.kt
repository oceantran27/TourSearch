package com.example.flybooking.navigation

enum class AppScreens {
    Home,
//    SearchResults,
    Explore,
    Bookmarks,
    Profile
}

fun onNavBar(screen: AppScreens): Boolean {
    return screen == AppScreens.Home || screen == AppScreens.Explore || screen == AppScreens.Bookmarks || screen == AppScreens.Profile
}