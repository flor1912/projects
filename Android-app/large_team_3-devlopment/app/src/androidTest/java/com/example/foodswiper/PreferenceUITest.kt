package com.example.foodswiper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Test

import org.junit.Rule

class PreferenceUITest {
    @JvmField
    @Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testUiElements() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("PreferenceBox").assertIsDisplayed()
    }

    @Test
    fun locationExposedDropDownMenu_verifyIsClicked() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("LocationExposedDropDownMenuBox").performClick()
        composeTestRule.onNodeWithTag("LocationExposedDropDownMenu").assertIsDisplayed()
    }

    @Test
    fun eatingHabitsExposedDropDownMenu_verifyIsClicked() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("EatingHabitsExposedDropDownMenuBox").performClick()
        composeTestRule.onNodeWithTag("EatingHabitsExposedDropDownMenu").assertIsDisplayed()
    }

    @Test
    fun addKeywords_verifyIsClicked() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("AddKeywords").performClick()
        composeTestRule.onNodeWithTag("Dialog").assertIsDisplayed()
    }

    @Test
    fun addKeywords_verifyKeywordAdded() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("AddKeywords").performClick()
        composeTestRule.onNodeWithTag("Dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("KeywordInput").performTextInput("Burger")
        composeTestRule.onNodeWithTag("SaveKeywordButton").performClick()
        composeTestRule.onNodeWithText("Burger").assertIsDisplayed()
    }

    @Test
    fun addKeywords_verifyMultipleKeywordsAddedAndOneDeleted() {
        composeTestRule.onNodeWithTag("PreferencesNavItem").performClick()
        composeTestRule.onNodeWithTag("AddKeywords").performClick()
        composeTestRule.onNodeWithTag("Dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("KeywordInput").performTextInput("Burger")
        composeTestRule.onNodeWithTag("SaveKeywordButton").performClick()
        composeTestRule.onNodeWithText("Burger").assertIsDisplayed()

        composeTestRule.onNodeWithTag("AddKeywords").performClick()
        composeTestRule.onNodeWithTag("Dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("KeywordInput").performTextInput("Pizza")
        composeTestRule.onNodeWithTag("SaveKeywordButton").performClick()
        composeTestRule.onNodeWithText("Pizza").assertIsDisplayed()

        composeTestRule.onNodeWithText("Burger").performClick()
        composeTestRule.onNodeWithText("Burger").assertDoesNotExist()
    }


}