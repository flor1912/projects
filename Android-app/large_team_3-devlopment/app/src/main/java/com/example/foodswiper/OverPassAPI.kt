package com.example.foodswiper


import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


data class RestaurantData(
    var id: Long,
    var lat: Double,
    var lon: Double,
    var tags: TagData?
)

data class TagData(
    var amenity: String,
    var name: String,
    var cuisine: String,
    var opening_hours: String,
    var wheelchair: String,
    var delivery: String,
    var takeaway: String,
    @SerializedName("contact:website") var contactWebsite: String

)

data class Elements(
    @SerializedName("elements"  ) var elements: ArrayList<RestaurantData>? = arrayListOf()
)

interface OverPassApi {
    @Headers(
        "Accept: application/json"
    )
    @GET("interpreter")
    fun getRestaurantData(@Query("data") string: String): Call<Elements?>?
}

fun buildQueryString(store: PreferenceStorageClass): String {

    val foodChoice = store.getFoodChoiceIndex()
    val outputTypeString = "[out:json];"
    val areaString = "area[name=\"Graz\"];"
    val amenityString = "node[\"amenity\"=\"restaurant\"]"
    var foodChoiceString = ""
    val lastOutString = "(area);out;"
    if (foodChoice == 1) {
        foodChoiceString = "[\"diet:vegetarian\"]"
    } else if (foodChoice == 2) {
        foodChoiceString = "[\"diet:vegan\"]"
    }
    return outputTypeString + areaString + amenityString + foodChoiceString + lastOutString
}