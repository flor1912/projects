package com.example.foodswiper

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreferenceScreen(navController: NavController) {
    val context = LocalContext.current
    val store = PreferenceStorageClass(context.dataStore)
    val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

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
                Text(
                    text = "Basics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 40.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 60.dp)
                ) {
                    Text(
                        text = "Location",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 15.dp)


                    )
                    OutlinedTextField(
                        value = sharedPreferences.getString("city", "") ?: "Graz",
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 30.dp)
                            .size(50.dp)


                    )
                    IconButton(
                        onClick = { navController.navigate("Choose_location") },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Find Location"
                        )
                    }
                }
                Row(
                    modifier = Modifier.padding(top = 120.dp)
                ) {
                    Text(
                        text = "Distance",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 5.dp, end = 10.dp)
                    )
                    val distanceIndex: Int = store.getDistanceIndex()

                    val items = listOf("1km", "5km ", "10km", "25km", "50km")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedItem by rememberSaveable { mutableStateOf(items[distanceIndex]) }

                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .testTag("LocationExposedDropDownMenuBox")
                            .weight(1f)
                            .offset((-4).dp)
                            .padding(end = 40.dp),

                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        TextField(
                            modifier = Modifier
                                .clip(RoundedCornerShape(25.dp)),

                            readOnly = true,
                            value = selectedItem,
                            onValueChange = { },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                backgroundColor = Color(0xFFA9E1BB)


                            ),

                            )
                        ExposedDropdownMenu(
                            modifier = Modifier.testTag("LocationExposedDropDownMenu"),
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            items.forEachIndexed {index, selectionOption ->
                                DropdownMenuItem(

                                    onClick = {
                                        selectedItem = selectionOption
                                        expanded = false
                                        CoroutineScope(Dispatchers.IO).launch {
                                            store.updateDistanceValueIndex(index)
                                            Log.i("tag", store.getDistanceIndex().toString() + "index")

                                        }
                                    }

                                ) {
                                    Text(text = selectionOption)

                                }
                            }
                        }
                    }
                }
                Text(
                    text = "Lifestyle",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 175.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 215.dp)
                ) {
                    Text(
                        text = "Eating Habits",
                        fontSize = 20.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 10.dp, end = 10.dp)

                    )
                    val foodChoiceIndex: Int = store.getFoodChoiceIndex()
                    val items = listOf("Everything", "Vegetarian", "Vegan")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedItem by remember { mutableStateOf(items[foodChoiceIndex]) }

                    ExposedDropdownMenuBox(
                        modifier = Modifier
                            .testTag("EatingHabitsExposedDropDownMenuBox")
                            .weight(1f)
                            .offset((-4).dp)
                            .padding(end = 30.dp),

                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {
                        TextField(
                            modifier = Modifier
                                .clip(RoundedCornerShape(25.dp)),

                            readOnly = true,
                            value = selectedItem,
                            onValueChange = { },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                backgroundColor = Color(0xFFA9E1BB)

                            )
                        )
                        ExposedDropdownMenu(
                            modifier = Modifier.testTag("EatingHabitsExposedDropDownMenu"),

                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            items.forEachIndexed { index,selectionOption ->
                                DropdownMenuItem(

                                    onClick = {
                                        selectedItem = selectionOption
                                        expanded = false
                                        CoroutineScope(Dispatchers.IO).launch {
                                            store.updateFoodChoiceValueIndex(index)
                                            Log.i("tag", store.getFoodChoiceIndex().toString() + "index")

                                        }
                                    }
                                ) {
                                    Text(text = selectionOption)
                                }
                            }
                        }
                    }
                }
                val temp = remember {
                    mutableStateOf("")
                }
                var keywordsState by remember {
                    mutableStateOf(listOf(""))
                }
                val dialogShowing = remember {
                 mutableStateOf(false)
                }
                if(dialogShowing.value){
                    MyDialog {
                        temp.value = it
                        dialogShowing.value = false
                        if(temp.value != "") {
                            keywordsState = keywordsState + listOf(temp.value)
                        }
                    }
                }
                Text(
                    text = "Keywords",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 270.dp)
                )
                LazyRow( modifier = Modifier
                            .padding(top = 310.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    items(keywordsState.size) { index ->
                        if (keywordsState[index] != "")
                        {
                            Card( modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterStart)
                                    .height(40.dp)
                                    .width(110.dp)
                                    .size(100.dp), shape = RoundedCornerShape(10.dp),
                                backgroundColor = Color(0xFFA9E1BB)
                            )
                            {
                                Text( text = keywordsState[index],
                                      textAlign = TextAlign.Start,
                                      modifier = Modifier.padding(8.dp)
                                    )
                                IconButton(onClick =
                                { keywordsState = keywordsState - listOf(keywordsState[index]).toSet() },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                                {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .size(20.dp))
                                }
                            }
                        }
                    }
                }
                IconButton(
                    onClick = { dialogShowing.value = true},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                        .testTag("AddKeywords")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp),
                        tint = Color(0xFF000000)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 500.dp, bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pizza),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(150.dp)
                    .height(100.dp)
            )
        }
        FloatingActionButton(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    store.updateFirstStartupComplete(true)
            } },modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF000000),
            )
        }
    }
}

@Composable
fun MyDialog( onDismiss :(value:String) -> Unit){
    val keyword = remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = {onDismiss("") }) {
        Column(
            modifier = Modifier
                .testTag("Dialog")
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(modifier = Modifier.testTag("KeywordInput"), value = keyword.value, onValueChange = {keyword.value = it })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onDismiss("")}) {
                    Text(text = "Cancel")
                }
                Button(modifier = Modifier.testTag("SaveKeywordButton"), onClick = { onDismiss(keyword.value) }) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPreferenceScreen() {
    val navController = rememberNavController()
    PreferenceScreen(navController)
}