package com.example.foodswiper

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

private const val USER_PREFERENCES_NAME = "user_preference_setting"
val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

var temporal : Boolean = false

open class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        val sharedPreferences : SharedPreferences = this.getSharedPreferences("Preference", MODE_PRIVATE)
        val start = sharedPreferences.getBoolean("Preference", true)
        setContent {
            temporal = start
            if(start)
            {
                val editor = sharedPreferences.edit()
                editor.putBoolean("Preference", false)
                editor.apply()
            }
            MainScreen()
        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA9E1BB))
    ) {
        Scaffold(
            bottomBar = { BottomNavMenu(navController) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    if(temporal){
                        MenuNavigation1(navController = navController)
                    }else
                    {
                        MenuNavigation(navController = navController)
                    }
                }
            }
        )
    }
}


@Composable
fun BottomNavMenu(navController: NavController) {
    val items = listOf(
        BottomNavMenuItem.Preferences,
        BottomNavMenuItem.Discover,
        BottomNavMenuItem.Favorites,
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.black),
        contentColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                modifier = Modifier
                    .testTag(item.title + "NavItem"),
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title, color = Color.White.copy(0.5f)) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.5f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MenuNavigation1(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavMenuItem.Preferences.route) {
        composable(BottomNavMenuItem.Preferences.route) {
            PreferenceScreen(navController)
        }
        composable(BottomNavMenuItem.Discover.route) {
            DiscoverScreen()
        }
        composable(BottomNavMenuItem.Favorites.route) {
            FavoriteScreen()
        }
    }
}

@Composable
fun MenuNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavMenuItem.Discover.route) {
        composable(BottomNavMenuItem.Discover.route) {
            DiscoverScreen()
        }
        composable(BottomNavMenuItem.Preferences.route) {
            PreferenceScreen(navController)
        }
        composable(BottomNavMenuItem.Favorites.route) {
            FavoriteScreen()
        }
        composable("Choose_location"){
            ChooseLocationScreen(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}