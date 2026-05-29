package com.bold.feature.home.presentation.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bold.core.model.location.LocationData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(instrumentedPackages = ["androidx.loader.content"], sdk = [34])
class SearchBarComponentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchBar_displaysHint_whenEmpty() {
        composeTestRule.setContent {
            SearchBarComponent(
                query = "",
                onQueryChange = {},
                onClearSearch = {},
                isSearching = false,
                searchResults = emptyList(),
                onLocationSelected = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Clear").assertDoesNotExist()
    }

    @Test
    fun searchBar_typeQuery_invokesCallbackAndShowsClearButton() {
        var typedQuery = ""
        composeTestRule.setContent {
            SearchBarComponent(
                query = "Bo",
                onQueryChange = { typedQuery = it },
                onClearSearch = {},
                isSearching = false,
                searchResults = emptyList(),
                onLocationSelected = {}
            )
        }

        val textField = composeTestRule.onNode(hasSetTextAction())
        textField.performTextInput("g")
        
        // Since the current query is "Bo", appending "g" without cursor moves it to the start, making it "gBo"
        assertEquals("gBo", typedQuery)
        
        // Clear button should be visible because query is not empty
        composeTestRule.onNodeWithContentDescription("Clear").assertIsDisplayed()
    }

    @Test
    fun searchBar_clickClear_invokesClearCallback() {
        var clearClicked = false
        composeTestRule.setContent {
            SearchBarComponent(
                query = "Bogota",
                onQueryChange = {},
                onClearSearch = { clearClicked = true },
                isSearching = false,
                searchResults = emptyList(),
                onLocationSelected = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Clear").performClick()
        assertTrue(clearClicked)
    }

    @Test
    fun searchBar_showsResults_andHandlesClick() {
        var selectedLocation: LocationData? = null
        val mockLocations = listOf(
            LocationData("Bogota", "Colombia", 4.6, -74.0),
            LocationData("Boston", "USA", 42.3, -71.0)
        )
        
        composeTestRule.setContent {
            SearchBarComponent(
                query = "Bo",
                onQueryChange = {},
                onClearSearch = {},
                isSearching = false,
                searchResults = mockLocations,
                onLocationSelected = { selectedLocation = it }
            )
        }

        composeTestRule.onNodeWithText("Bogota").assertIsDisplayed()
        composeTestRule.onNodeWithText("Colombia").assertIsDisplayed()
        composeTestRule.onNodeWithText("Boston").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Bogota").performClick()
        assertEquals("Bogota", selectedLocation?.name)
    }
}
