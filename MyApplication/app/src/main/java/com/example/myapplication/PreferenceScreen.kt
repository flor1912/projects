@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.myapplication.PreferenceStorageClass

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceScreen() {
    Surface {

        val context  = LocalContext.current
        val store = PreferenceStorageClass(context.dataStore)
        val prefs by store.getUserPreferencesFlow.collectAsState(
            initial = UserPreferences(false, "Graz", "Austria", 0, 0, emptySet())
        )

        var city by remember(prefs.city) { mutableStateOf(prefs.city) }
        var country by remember(prefs.country) { mutableStateOf(prefs.country) }
        val scope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFA9E1BB))
                .testTag("PreferenceBox")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                Box(
                    modifier = Modifier
                        .width(370.dp)
                        .height(400.dp)
                        .background(Color(0xFF8FD7A6))
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "Preferences",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Row(
                        modifier = Modifier.padding(top = 45.dp)
                    ) {
                        Text(
                            text = "City",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 15.dp)

                        )
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 30.dp)
                                .fillMaxWidth()
                        )
                        IconButton(onClick = {
                            scope.launch { store.updateCity(city) }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    }

                    Row(
                        modifier = Modifier.padding(top = 120.dp)
                    ) {
                        Text(
                            text = "Country",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 15.dp)

                        )
                        OutlinedTextField(
                            value = country,
                            onValueChange = { country = it },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 5.dp)
                                .fillMaxWidth()
                        )
                        IconButton(onClick = {
                            scope.launch { store.updateCountry(country) }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    }
                    Row(
                        modifier = Modifier.padding(top = 200.dp)
                    ) {
                        Text(
                            text = "Distance",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 5.dp, end = 10.dp)
                        )

                        // Read once for initial selection
                        val initialIndex = remember { store.getDistanceIndex() }
                        val items = listOf("1km", "5km", "10km", "25km", "50km")

                        var expanded by remember { mutableStateOf(false) }
                        var selectedIndex by rememberSaveable {
                            mutableStateOf(initialIndex.coerceIn(0, items.lastIndex))
                        }
                        val scope = rememberCoroutineScope()

                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .testTag("LocationExposedDropDownMenuBox")
                                .weight(1f)
                                .offset((-4).dp)
                                .padding(end = 40.dp),
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            // Anchor
                            TextField(
                                modifier = Modifier
                                    .menuAnchor() // <-- IMPORTANT for ExposedDropdownMenu
                                    .clip(RoundedCornerShape(25.dp)),
                                readOnly = true,
                                value = items[selectedIndex],
                                onValueChange = { },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color(0xFFA9E1BB),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )

                            ExposedDropdownMenu(
                                modifier = Modifier.testTag("LocationExposedDropDownMenu"),
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                items.forEachIndexed { index, option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },           // M3 1.0.x signature
                                        onClick = {
                                            selectedIndex = index
                                            expanded = false
                                            scope.launch {
                                                store.updateDistanceValueIndex(index) // persist
                                            }
                                            Log.i("Distance", "Selected index=$index")
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "Lifestyle",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 270.dp)
                    )
                    Row(
                        modifier = Modifier.padding(top = 320.dp)
                    ) {
                        Text(
                            text = "Eating Habits",
                            fontSize = 21.sp,
                            modifier = Modifier.padding(top = 10.dp, end = 10.dp)
                        )

                        val items = listOf("Everything", "Vegetarian", "Vegan")

                        // Read once for initial selection (don’t call on every recomposition)
                        val initialIndex =
                            remember { store.getFoodChoiceIndex().coerceIn(0, items.lastIndex) }

                        var expanded by remember { mutableStateOf(false) }
                        var selectedIndex by rememberSaveable { mutableStateOf(initialIndex) }
                        val scope = rememberCoroutineScope()

                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .testTag("EatingHabitsExposedDropDownMenuBox")
                                .offset((9).dp)
                                .padding(end = 20.dp),
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            // Color background via Surface for max compatibility
                            Surface(
                                color = Color(0xFFA9E1BB),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .menuAnchor() // IMPORTANT: anchors the menu to this field
                                        .clip(RoundedCornerShape(25.dp)),
                                    readOnly = true,
                                    singleLine = true,
                                    value = items[selectedIndex],
                                    onValueChange = { /* read-only anchor */ },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    // Hide the underline; avoid containerColor params that may not exist in older M3
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                            ExposedDropdownMenu(
                                modifier = Modifier.testTag("EatingHabitsExposedDropDownMenu"),
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                items.forEachIndexed { index, option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },           // M3 1.0.x signature
                                        onClick = {
                                            selectedIndex = index
                                            expanded = false
                                            scope.launch {
                                                store.updateFoodChoiceValueIndex(index) // persist selection
                                            }
                                            Log.i("EatingHabits", "Selected index=$index")
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
                KeywordsSection(store = store)
            }
        }
    }

}

@Composable
private fun KeywordsSection(store: PreferenceStorageClass) {
    val scope = rememberCoroutineScope()

    val prefs by store.getUserPreferencesFlow.collectAsState(
        initial = UserPreferences(false, "Graz", "Austria", 0, 0, emptySet())
    )
    val keywords = prefs.keywordsSet

    var dialogOpen by remember { mutableStateOf(false) }

    // ...same UI as above...

    if (dialogOpen) {
        KeywordDialog(
            onResult = { raw ->
                dialogOpen = false
                val value = raw.trim()
                if (value.isNotEmpty() && value !in keywords) {
                    scope.launch { store.addKeyword(value) }
                }
            },
            onCancel = { dialogOpen = false }
        )
    }
}


@Composable
private fun KeywordChip(text: String, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        // Material3 1.0.x compatible card colors:
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color(0xFFA9E1BB)
        )
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun KeywordDialog(
    onResult: (String) -> Unit,
    onCancel: () -> Unit
) {
    var keyword by remember { mutableStateOf("") }
    androidx.compose.ui.window.Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Add keyword") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                androidx.compose.material3.Button(onClick = onCancel) {
                    Text("Cancel")
                }
                androidx.compose.material3.Button(onClick = { onResult(keyword) }) {
                    Text("Save")
                }
            }
        }
    }
}

