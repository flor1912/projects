package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RestaurantCard(
    item: OsmElement,
    modifier: Modifier = Modifier
) {
    val name = item.tags?.get("name") ?: "(Unnamed)"
    val cuisine = item.tags?.get("cuisine") ?: "—"
    val opening = item.tags?.get("opening_hours") ?: "—"

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp).background(Color(0xFF8FD7A6))) {
            Text(name, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(25.dp))
            Text("Cuisine: $cuisine")
            Text("Hours: $opening")
            Spacer(Modifier.weight(1f))

            // Optional: show coordinates for debugging
            Text("(${item.lat}, ${item.lon})", style = MaterialTheme.typography.bodySmall)
        }
    }
}
