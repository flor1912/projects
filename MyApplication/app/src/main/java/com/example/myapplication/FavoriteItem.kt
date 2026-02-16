package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteItem (
    val id: Long? = null,
    val name: String,
    val lat: Double,
    val lon: Double
    )