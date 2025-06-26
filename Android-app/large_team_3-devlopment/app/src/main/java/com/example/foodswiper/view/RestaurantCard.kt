package com.example.foodswiper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodswiper.R
import com.example.foodswiper.RestaurantData

@Composable
fun RestaurantCard(
    modifier: Modifier,
    restaurant: RestaurantData,
) {
    Card(modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF8FD7A6))
                .padding(start = 10.dp)
                .testTag("DiscoverLayoutBox")
        ) {
            Column(Modifier.padding(16.dp)) {
                Box {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = restaurant.tags?.name.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(Modifier.padding(16.dp)) {
                Box {
                    val uriHandler = LocalUriHandler.current
                    ClickableText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        onClick = {
                            if (restaurant.tags?.contactWebsite != null)
                            {
                                uriHandler.openUri(restaurant.tags?.contactWebsite.toString())
                            }},
                        text = if (restaurant.tags?.contactWebsite != null)
                        {
                            AnnotatedString(restaurant.tags?.contactWebsite.toString())
                        } else {
                            AnnotatedString("")
                        },
                        style = TextStyle(
                            textAlign = TextAlign.Center),
                    )
                }
            }
            Column(Modifier.padding(top = 140.dp)) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        text = if (restaurant.tags?.opening_hours != null)
                        {
                            "Opening Time:\n" + restaurant.tags?.opening_hours.toString()
                        } else {
                            "Opening Time: Unknown"
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,

                        )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        text = if (restaurant.tags?.cuisine != null)
                        {
                            "Cuisine: " + restaurant.tags?.cuisine.toString()
                        } else {
                            "Cuisine: Unknown"
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        text = if (restaurant.tags?.takeaway != null)
                        {
                            "Takeaway: " + restaurant.tags?.takeaway.toString()
                        } else {
                            "Takeaway: Unknown"
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        text = if (restaurant.tags?.delivery != null)
                        {
                            "Delivery: " + restaurant.tags?.delivery.toString()
                        } else {
                            "Delivery: Unknown"
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        text = "Accessibility:",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                    )
                    Box(modifier= Modifier.fillMaxWidth()
                        .padding(vertical = 10.dp))
                    {

                        Icon(
                            painter = if (restaurant.tags?.wheelchair == "yes"){
                                painterResource(id = R.drawable.ic_wheelchair_accessible)
                            }
                            else{
                                painterResource(id = R.drawable.ic_wheelchair_nonaccessible)
                                },
                            contentDescription = null,
                            modifier= Modifier.size(45.dp)
                                .align(Alignment.Center),
                            tint = Color.Black
                        )
                    }
                }
        }
    }
}


@Composable
fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
    color: Color,
    iconColor: Color
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .size(70.dp),
        onClick = onClick
    ) {
        Icon(icon, null,
            modifier= Modifier.size(45.dp),
            tint = iconColor)
    }
}



private val dummyRestaurant = RestaurantData(0,0.0,0.0,null)

@Preview
@Composable
fun PreviewDiscoverySlideScreen() {
  RestaurantCard(Modifier, dummyRestaurant)
}