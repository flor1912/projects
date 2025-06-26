package com.example.foodswiper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

import org.junit.Test

import org.junit.Rule


class BottomNavigationUITest {
    @JvmField
    @Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule.onNodeWithTag("DiscoverBox").assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyGoToPreferences() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("PreferenceBox").assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyGoToFavorites() {
        composeTestRule.onNodeWithTag("FavoritesNavItem").performClick()
        composeTestRule.onNodeWithTag("FavoritesBox").assertIsDisplayed()
    }

    @Test
    fun appNavHost_verifyGoToAndBack() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("PreferenceBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DiscoverNavItem").performClick()
        composeTestRule.onNodeWithTag("DiscoverBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FavoritesNavItem").performClick()
        composeTestRule.onNodeWithTag("FavoritesBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DiscoverNavItem").performClick()
        composeTestRule.onNodeWithTag("DiscoverBox").assertIsDisplayed()
    }
}