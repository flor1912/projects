package com.example.foodswiper

sealed class BottomNavMenuItem(var route: String, var icon: Int, var title: String) {
    object Preferences : BottomNavMenuItem("preferences", R.drawable.ic_preferences, "Preferences")
    object Discover : BottomNavMenuItem("discover", R.drawable.ic_discover, "Discover")
    object Favorites : BottomNavMenuItem("favorites", R.drawable.ic_favorites, "Favorites")
}