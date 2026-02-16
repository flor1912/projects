package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UI {
    @Headers("Accept: application/json")
    @GET("interpreter")
    suspend fun query(@Query("data") q: String): OverpassResponse
}