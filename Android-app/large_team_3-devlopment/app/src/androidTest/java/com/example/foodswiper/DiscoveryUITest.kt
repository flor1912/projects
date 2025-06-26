package com.example.foodswiper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

/*
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
*/

class DiscoveryUITest {
  @JvmField
  @Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testUiElements() {
    composeTestRule.setContent { DiscoverScreen() }
    composeTestRule.onNodeWithTag("DiscoverBox").assertIsDisplayed()
  }

  /*
  @Test
  fun testFakeElements(){
    composeTestRule.setContent { DiscoverScreen() }

    composeTestRule.onNodeWithText("Restaurant 1").assertIsDisplayed()
    composeTestRule.onNodeWithText("Restaurant 1").performGesture {
      swipeLeft()
    }
    composeTestRule.onNodeWithText("Restaurant 2").assertIsDisplayed()
    composeTestRule.onNodeWithText("Restaurant 2").performGesture {
      swipeRight()
    }
    composeTestRule.onNodeWithText("Restaurant 3").assertIsDisplayed()
    composeTestRule.onNodeWithText("Restaurant 3").performGesture {
      swipeLeft()
    }
  }
   */
}