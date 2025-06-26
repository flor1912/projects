package com.example.foodswiper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountriesService {
    @GET("countryInfoJSON")
    fun getCountries(@Query("username") username: String): Call<CountryInfo>

    @GET("childrenJSON")
    fun getStates(@Query("geonameId") geonameId: Long, @Query("username") username: String): Call<StateInfo>

}
