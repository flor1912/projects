package com.example.myapplication

class RestaurantRepository(private val api: UI) {
    suspend fun fetchRestaurants(query: String): List<OsmElement> {
        return api.query(query).elements
            .filter { it.lat != null && it.lon != null } // only nodes with coords
    }
}