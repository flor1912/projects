package com.example.foodswiper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


var likedRestaurants = ArrayList<RestaurantData>()
var dislikedRestaurants = ArrayList<RestaurantData>()


@Composable
fun FavoriteScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA9E1BB))
            .testTag("FavoritesBox")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(400.dp)
                    .background(Color(0xFF8FD7A6))
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Favorites",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFavoriteScreen() {
    FavoriteScreen()
}

