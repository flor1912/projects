package com.example.foodswiper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.foodswiper.PreferenceStorageClass.PreferencesKeys.DISTANCE_VALUE_INDEX
import com.example.foodswiper.PreferenceStorageClass.PreferencesKeys.FIRST_STARTUP_COMPLETE
import com.example.foodswiper.PreferenceStorageClass.PreferencesKeys.FOOD_CHOICE_VALUE_INDEX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

data class UserPreferences(
    val firstStartupComplete: Boolean,
    val distanceValueIndex: Int,
    val foodChoiceValueIndex: Int
)
class PreferenceStorageClass(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val FIRST_STARTUP_COMPLETE = booleanPreferencesKey("first_startup_complete")
        val DISTANCE_VALUE_INDEX = intPreferencesKey("distance_value_index")
        val FOOD_CHOICE_VALUE_INDEX = intPreferencesKey("food_choice_value_index")
    }

    val getUserPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map {preferences ->
            UserPreferences(
                firstStartupComplete = preferences[FIRST_STARTUP_COMPLETE] ?: false,
                distanceValueIndex = preferences[DISTANCE_VALUE_INDEX] ?: 0,
                foodChoiceValueIndex = preferences[FOOD_CHOICE_VALUE_INDEX] ?: 0
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
}