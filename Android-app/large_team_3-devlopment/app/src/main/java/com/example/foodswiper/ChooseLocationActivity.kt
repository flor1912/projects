package com.example.foodswiper

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Data classes that are needed to get Data from API
@Serializable
data class CountryInfo(
    val geonames: List<Country>
)

@Serializable
data class StateInfo(
    val geonames: List<State>
)

@Serializable
data class Country(
    val countryName: String,
    val geonameId: Long
)

@Serializable
data class State(
    val toponymName: String,
    val geonameId: Long
)


// Set up retrofit client to make API request
const val BASE_URL = "http://api.geonames.org/"

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val countriesService: CountriesService = retrofit.create(CountriesService::class.java)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChooseLocationScreen(navController: NavController) {

    // Necessary variables
    val allCountries = remember { mutableStateOf<List<Country>>(emptyList()) }
    var expandedCountry by remember { mutableStateOf(false) }
    var expandedState by remember { mutableStateOf(false) }
    var expandedCity by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var states by remember { mutableStateOf<List<State>>(emptyList()) }
    var cities by remember { mutableStateOf<List<State>>(emptyList()) }
    var switchOn by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    // Load and save countries in "allCountries" variable
    LaunchedEffect(Unit) {
        val countries = getCountriesFromAPI()
        allCountries.value = countries

        selectedCountry = sharedPreferences.getString("country", "") ?: ""
        selectedState = sharedPreferences.getString("state", "") ?: ""
        selectedCity = sharedPreferences.getString("city", "") ?: ""
        switchOn = sharedPreferences.getBoolean("switch", true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA9E1BB))
            .testTag("ChooseLocationBox")
    ) {
        IconButton(
            onClick = { navController.navigate(BottomNavMenuItem.Preferences.route) },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back"
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Box(
                modifier = Modifier
                    .width(370.dp)
                    .height(360.dp)
                    .background(Color(0xFF8FD7A6))
                    .padding(start = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Location",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.location_icon),
                            contentDescription = "Location Icon",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "My current Location",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = switchOn,
                            onCheckedChange = { switchOn = it }
                        )
                    }
                    // Only show next part if switch off
                    if(!switchOn) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Country ", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(120.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedCountry,
                                onExpandedChange = { expandedCountry = !expandedCountry },
                                modifier = Modifier.width(150.dp)
                            ) {
                                TextField(
                                    value = selectedCountry,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry)
                                    },
                                    modifier = Modifier
                                        .background(color = Color(0xFFA9E1BB))
                                        .border(1.dp, color = Color.Black, shape = RectangleShape)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCountry,
                                    onDismissRequest = { expandedCountry = false }) {
                                    allCountries.value.forEach { country ->
                                        DropdownMenuItem(onClick = {
                                            selectedCountry = country.countryName
                                            // load and save associated states
                                            coroutineScope.launch {
                                                states = getStatesAndCitiesFromAPI(country.geonameId)
                                            }
                                            expandedCountry = false
                                        }) {
                                            Text(text = country.countryName)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "State", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(140.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedState,
                                onExpandedChange = { expandedState = !expandedState },
                                modifier = Modifier.width(150.dp)
                            ) {
                                TextField(
                                    value = selectedState,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)
                                    },
                                    modifier = Modifier
                                        .background(color = Color(0xFFA9E1BB))
                                        .border(1.dp, color = Color.Black, shape = RectangleShape)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedState,
                                    onDismissRequest = { expandedState = false }) {
                                    states.forEach { state ->
                                        DropdownMenuItem(onClick = {
                                            selectedState = state.toponymName
                                            // load and save associated cities
                                            coroutineScope.launch {
                                                cities = getStatesAndCitiesFromAPI(state.geonameId)
                                            }
                                            expandedState = false
                                        }) {
                                            Text(text = state.toponymName)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "City", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(148.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedCity,
                                onExpandedChange = { expandedCity = !expandedCity },
                                modifier = Modifier.width(150.dp)
                            ) {
                                TextField(
                                    value = selectedCity,
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCity)
                                    },
                                    modifier = Modifier
                                        .background(color = Color(0xFFA9E1BB))
                                        .border(1.dp, color = Color.Black, shape = RectangleShape)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedCity,
                                    onDismissRequest = { expandedCity = false }) {
                                    cities.forEach { city ->
                                        DropdownMenuItem(onClick = {
                                            selectedCity = city.toponymName
                                            expandedState = false
                                        }) {
                                            Text(text = city.toponymName)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // in case selected items change
    DisposableEffect(Unit) {
        onDispose {
            val editor = sharedPreferences.edit()
            editor.putString("country", selectedCountry)
            editor.putString("state", selectedState)
            editor.putString("city", selectedCity)
            editor.putBoolean("switch", switchOn)
            editor.apply()
        }
    }
}



// API request to get countries
suspend fun getCountriesFromAPI(): List<Country> = withContext(Dispatchers.IO) {
    val call = countriesService.getCountries("emdjem")
    try {
        val response = call.execute()
        if (response.isSuccessful) {
            val countryInfo: CountryInfo? = response.body()
            val countries: List<Country>? = countryInfo?.geonames
            if (countries != null) {
                return@withContext countries
            }
        } else {
            Log.e("API", "Error: ${response.code()} ${response.message()}")
        }
    } catch (e: Exception) {
        Log.e("API", "Exception: ${e.message}")
    }
    return@withContext emptyList()
}

// API request to get states and cities
suspend fun getStatesAndCitiesFromAPI(geonameId: Long): List<State> = withContext(Dispatchers.IO) {
    val call = countriesService.getStates(geonameId, "emdjem")
    try {
        val response = call.execute()
        if (response.isSuccessful) {
            val stateAndCityInfo: StateInfo? = response.body()
            val statesAndCities: List<State>? = stateAndCityInfo?.geonames
            if (statesAndCities != null) {
                return@withContext statesAndCities
            }
        } else {
            Log.e("API", "Error: ${response.code()} ${response.message()}")
        }
    } catch (e: Exception) {
        Log.e("API", "Exception: ${e.message}")
    }
    return@withContext emptyList()
}


@Preview
@Composable
fun PreviewChooseLocationActivity() {
    val navController = rememberNavController()
    ChooseLocationScreen(navController)
}