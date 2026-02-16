package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val loading: Boolean = false,
    val items: List<OsmElement> = emptyList(),
    val error: String? = null
)

class DiscoverViewModel(
    private val repo: RestaurantRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(DiscoverUiState())
    val ui: StateFlow<DiscoverUiState> = _ui

    private var loadJob: Job? = null
    private var lastQueryKey: String? = null

    fun load(city: String, country: String, dietIndex: Int, keywords: Set<String>) {
        // Create a stable key for "same request"
        val key = buildString {
            append(city.trim().lowercase())
            append("|")
            append(country.trim().lowercase())
            append("|")
            append(dietIndex)
            append("|")
            // sort to avoid different order causing reload
            append(keywords.map { it.trim().lowercase() }.sorted().joinToString(","))
        }

        // ✅ If we already loaded this exact query and have data, don't reload
        if (key == lastQueryKey && _ui.value.items.isNotEmpty() && _ui.value.error == null) {
            return
        }
        lastQueryKey = key

        // ✅ Cancel any previous in-flight load
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            // ✅ Keep existing items while showing loading
            _ui.value = _ui.value.copy(loading = true, error = null)

            try {
                val q = buildOverpassQueryCityCountry(
                    city = city,
                    country = country,
                    dietIndex = dietIndex,
                    keywords = keywords
                )

                // ✅ small retry for flaky Overpass (504 etc.)
                val restaurants = retry(times = 3, initialDelayMs = 600) {
                    repo.fetchRestaurants(q)
                }

                _ui.value = DiscoverUiState(items = restaurants)
            } catch (t: Throwable) {
                _ui.value = _ui.value.copy(
                    loading = false,
                    error = t.message ?: "Unknown error"
                )
            }
        }
    }

    private suspend fun <T> retry(times: Int, initialDelayMs: Long, block: suspend () -> T): T {
        var last: Throwable? = null
        var delayMs = initialDelayMs
        repeat(times) { attempt ->
            try {
                return block()
            } catch (t: Throwable) {
                last = t
                if (attempt < times - 1) {
                    delay(delayMs)
                    delayMs *= 2
                }
            }
        }
        throw last ?: RuntimeException("Retry failed")
    }
}