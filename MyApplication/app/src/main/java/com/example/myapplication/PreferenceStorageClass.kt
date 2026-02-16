package com.example.myapplication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.myapplication.PreferenceStorageClass.PreferencesKeys.CITY_VALUE_INDEX
import com.example.myapplication.PreferenceStorageClass.PreferencesKeys.COUNTRY_VALUE_INDEX
import com.example.myapplication.PreferenceStorageClass.PreferencesKeys.DISTANCE_VALUE_INDEX
import com.example.myapplication.PreferenceStorageClass.PreferencesKeys.FIRST_STARTUP_COMPLETE
import com.example.myapplication.PreferenceStorageClass.PreferencesKeys.FOOD_CHOICE_VALUE_INDEX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class UserPreferences(
    val firstStartupComplete: Boolean,
    val city: String,
    val country: String,
    val distanceValueIndex: Int,
    val foodChoiceValueIndex: Int,
    val keywordsSet: Set<String> = emptySet()

)
class PreferenceStorageClass(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val FIRST_STARTUP_COMPLETE = booleanPreferencesKey("first_startup_complete")
        val CITY_VALUE_INDEX = stringPreferencesKey("city_value_index")
        val COUNTRY_VALUE_INDEX = stringPreferencesKey("country_value_index")
        val DISTANCE_VALUE_INDEX = intPreferencesKey("distance_value_index")
        val FOOD_CHOICE_VALUE_INDEX = intPreferencesKey("food_choice_value_index")
        val KEYWORDS = stringSetPreferencesKey("keywordsSet")

    }

    val getUserPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map {preferences ->
            UserPreferences(
                firstStartupComplete = preferences[FIRST_STARTUP_COMPLETE] ?: false,
                city = preferences[CITY_VALUE_INDEX] ?: "Graz",
                country = preferences[COUNTRY_VALUE_INDEX] ?: "Austria",
                distanceValueIndex = preferences[DISTANCE_VALUE_INDEX] ?: 0,
                foodChoiceValueIndex = preferences[FOOD_CHOICE_VALUE_INDEX] ?: 0,
                keywordsSet = preferences[PreferencesKeys.KEYWORDS] ?: emptySet()
            )

        }


    suspend fun updateFirstStartupComplete(firstStartupComplete: Boolean) {
        dataStore.edit { preferences ->
            preferences[FIRST_STARTUP_COMPLETE] = firstStartupComplete
        }
    }
    suspend fun updateDistanceValueIndex(distanceValueIndex: Int) {
        dataStore.edit { preferences ->
            preferences[DISTANCE_VALUE_INDEX] = distanceValueIndex
        }
    }
    suspend fun updateFoodChoiceValueIndex(foodChoiceValueIndex: Int) {
        dataStore.edit { preferences ->
            preferences[FOOD_CHOICE_VALUE_INDEX] = foodChoiceValueIndex
        }
    }

    fun getStartupFlag() = runBlocking {
        dataStore.data.first()[FIRST_STARTUP_COMPLETE] ?: false
    }

    fun getDistanceIndex() = runBlocking {
        dataStore.data.first()[DISTANCE_VALUE_INDEX] ?: 0
    }

    fun getFoodChoiceIndex() = runBlocking {
        dataStore.data.first()[FOOD_CHOICE_VALUE_INDEX] ?: 0
    }

    suspend fun addKeyword(keyword: String) {
        dataStore.edit { prefs ->
            val set = prefs[PreferencesKeys.KEYWORDS]?.toMutableSet() ?: mutableSetOf()
            set += keyword
            prefs[PreferencesKeys.KEYWORDS] = set
        }
    }

    suspend fun removeKeyword(keyword: String) {
        dataStore.edit { prefs ->
            val set = prefs[PreferencesKeys.KEYWORDS]?.toMutableSet() ?: mutableSetOf()
            set -= keyword
            prefs[PreferencesKeys.KEYWORDS] = set
        }
    }

    suspend fun clearKeywords() {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.KEYWORDS] = emptySet()
        }
    }

    suspend fun updateCity(city: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.CITY_VALUE_INDEX] = city
        }
    }

    suspend fun updateCountry(country: String) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.COUNTRY_VALUE_INDEX] = country
        }
    }

    private val FAVORITES_KEY = stringSetPreferencesKey("favorites_json")

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    val favoritesFlow: Flow<List<FavoriteItem>> = dataStore.data.map { prefs ->
        val set = prefs[FAVORITES_KEY] ?: emptySet()
        set.mapNotNull { str ->
            runCatching { json.decodeFromString<FavoriteItem>(str) }.getOrNull()
        }.sortedBy { it.name.lowercase() }
    }

    suspend fun addFavorite(item: FavoriteItem) {
        val encoded = json.encodeToString(item)
        dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = current + encoded
        }
    }

    suspend fun removeFavorite(item: FavoriteItem) {
        val encoded = json.encodeToString(item)
        dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = current - encoded
        }
    }

    suspend fun clearFavorites() {
        dataStore.edit { prefs -> prefs.remove(FAVORITES_KEY) }
    }

}