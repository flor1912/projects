package com.example.foodswiper

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.example.foodswiper.view.CircleButton
import com.example.foodswiper.view.RestaurantCard
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun DiscoverScreen() {
    val context = LocalContext.current
    val store = PreferenceStorageClass(context.dataStore)
    val elementArray = remember {
        mutableStateListOf(RestaurantData(0,0.0,0.0,null))
    }
    LaunchedEffect(Unit) {
        sendRequest(elementArray, store)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA9E1BB))
            .testTag("DiscoverBox")
    ) {
        // Tinder-like swiping was implemented by importing https://github.com/alexstyl/compose-tinder-card and following its tutorial
        Box {
            val states = elementArray.reversed()
                .map { it to rememberSwipeableCardState() }

            val scope = rememberCoroutineScope()
            Box(
                Modifier
                    .padding(24.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                states.forEach { (restaurant, state) ->
                    if (state.swipedDirection == null) {
                        RestaurantCard(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("RestaurantCard")
                                .swipableCard(
                                    state = state,
                                    blockedDirections = listOf(Direction.Down),
                                    onSwiped = {},
                                ),
                            restaurant = restaurant
                        )
                    }
                    LaunchedEffect(restaurant, state.swipedDirection) {
                        if (state.swipedDirection != null) {
                            if (state.swipedDirection == Direction.Left)
                            {
                                dislike(restaurant)
                            }
                            if (state.swipedDirection == Direction.Right)
                            {
                                like(restaurant)
                            }
                        }
                    }
                }
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 70.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircleButton(
                    onClick = {
                        scope.launch {
                            val last = states.reversed()
                                .firstOrNull {
                                    it.second.offset.value == Offset(0f, 0f)
                                }?.second
                            last?.swipe(Direction.Left)
                        }
                    },
                    icon = Icons.Rounded.Close,
                    color = Color(204, 71, 78),
                    iconColor = Color(207, 159, 162)
                )
                CircleButton(
                    onClick = {
                        scope.launch {
                            val last = states.reversed()
                                .firstOrNull {
                                    it.second.offset.value == Offset(0f, 0f)
                                }?.second

                            last?.swipe(Direction.Right)
                        }
                    },
                    icon = Icons.Rounded.Favorite,
                    color = Color(11, 176, 12),
                    iconColor = Color(185, 237, 185)
                )
            }
        }
    }
}

private fun like(restaurant: RestaurantData)
{
    //TODO save likes
    likedRestaurants.add(restaurant)
}

private fun dislike(restaurant: RestaurantData)
{
    //TODO save dislikes
    dislikedRestaurants.add(restaurant)
}


fun sendRequest(elementArray: SnapshotStateList<RestaurantData>, store: PreferenceStorageClass) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://overpass-api.de/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(OverPassApi::class.java)
    val string = buildQueryString(store)

    val call: Call<Elements?>? = api.getRestaurantData(string)

    call!!.enqueue(object : Callback<Elements?> {
        override fun onResponse(call: Call<Elements?>, response: Response<Elements?>) {
            if (response.isSuccessful) {
                Log.d("Discovery Screen", "Success!" + response.body().toString())
                elementArray.clear()
                elementArray.addAll(((response.body()!!.elements!!)))
            }
        }

        override fun onFailure(call: Call<Elements?>, t: Throwable) {
            Log.e("Discovery Screen", "Failure " + t.message.toString())
        }
    })
}


@Preview
@Composable
fun PreviewDiscoverScreen() {
    DiscoverScreen()
}

