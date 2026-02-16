package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import kotlinx.coroutines.launch

private data class DeckCard(
    val key: Long,
    val item: OsmElement
)

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun DiscoverScreen(store: PreferenceStorageClass) {
    val AppBackground = Color(0xFFA9E1BB)
    val AppPanel = Color(0xFF8FD7A6)

    val repo = remember { RestaurantRepository(OverpassService.api) }
    val factory = remember { DiscoverViewModelFactory(repo) }

    val vm: DiscoverViewModel = viewModel(factory = factory)

    val favorites by store.favoritesFlow.collectAsState(initial = emptyList())
    val favoriteKeys = remember(favorites) { favorites.mapNotNull { it.id }.toSet() }

    val prefs by store.getUserPreferencesFlow.collectAsState(
        initial = UserPreferences(
            firstStartupComplete = false,
            city = "Graz",
            country = "Austria",
            distanceValueIndex = 0,
            foodChoiceValueIndex = 0,
            keywordsSet = emptySet()
        )
    )

    // Fetch when relevant preferences change
    LaunchedEffect(prefs.city, prefs.country, prefs.foodChoiceValueIndex, prefs.keywordsSet) {
        vm.load(
            city = prefs.city,
            country = prefs.country,
            dietIndex = prefs.foodChoiceValueIndex,
            keywords = prefs.keywordsSet
        )
    }

    val ui by vm.ui.collectAsState()

    Surface {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFA9E1BB))
        ) {
            when {
                ui.loading && ui.items.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                ui.error != null && ui.items.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${ui.error}")
                    }
                }

                else -> {
                    var deck by remember(ui.items) {

                        mutableStateOf(
                            ui.items
                                .filter { it.lat != null && it.lon != null }
                                .filter { it.id == null || it.id !in favoriteKeys }
                                .filter { it.lat != null && it.lon != null }
                                .mapIndexed { index, item ->
                                    val stableKey = item.id
                                        ?: ("${item.lat},${item.lon},${item.tags?.get("name")}".hashCode()
                                            .toLong())
                                    DeckCard(key = stableKey, item = item)
                                }
                        )
                    }

                    if (deck.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No restaurants found")
                        }
                    }

                    val scope = rememberCoroutineScope()

                    val stateMap = remember { mutableMapOf<Long, SwipeableCardState>() }

                    fun removeCard(cardKey: Long) {
                        deck = deck.filterNot { it.key == cardKey }
                        stateMap.remove(cardKey)
                    }

                    fun onCardSwiped(card: DeckCard, direction: Direction) {
                        if (direction == Direction.Right) {
                            val fav = card.item.toFavoriteItem(prefs)
                            scope.launch { store.addFavorite(fav) }
                        }
                        removeCard(card.key)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {

                        deck.forEachIndexed { index, card ->
                            key(card.key) {

                                val cardState = rememberSwipeableCardState()

                                LaunchedEffect(card.key) {
                                    stateMap[card.key] = cardState
                                }

                                val fromTop = (deck.size - 1) - index

                                RestaurantCard(
                                    item = card.item,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth()
                                        .height(320.dp)
                                        // little stack offset so you can see there are many
                                        .offset(y = (fromTop * 6).dp)
                                        .swipableCard(
                                            state = cardState,
                                            onSwiped = { direction ->
                                                onCardSwiped(
                                                    card,
                                                    direction
                                                )
                                            },
                                            onSwipeCancel = { }
                                        )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 24.dp, vertical = 32.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CircleButton(
                                onClick = {
                                    val card = deck.lastOrNull() ?: return@CircleButton
                                    val state = stateMap[card.key] ?: return@CircleButton
                                    scope.launch {
                                        state.swipe(Direction.Left)
                                        onCardSwiped(card, Direction.Left)
                                    }
                                },
                                icon = Icons.Rounded.Close
                            )

                            CircleButton(
                                onClick = {
                                    val card = deck.lastOrNull() ?: return@CircleButton
                                    val state = stateMap[card.key] ?: return@CircleButton
                                    scope.launch {
                                        state.swipe(Direction.Right)
                                        onCardSwiped(card, Direction.Right)
                                    }
                                },
                                icon = Icons.Rounded.Favorite
                            )
                        }
                    }
                }
            }
        }

    }
}

private fun OsmElement.toFavoriteItem(prefs: UserPreferences): FavoriteItem {
    val name = this.tags?.get("name") ?: "(Unnamed)"
    return FavoriteItem(
        id = this.id,
        name = name,
        lat = this.lat!!,
        lon = this.lon!!
    )
}


@Composable
private fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .size(56.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
