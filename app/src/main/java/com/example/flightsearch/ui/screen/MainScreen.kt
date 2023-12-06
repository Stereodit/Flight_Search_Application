package com.example.flightsearch.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.theme.FlightSearchTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Flight Search")
                }
            )
        },
    ) { innerPadding ->

        // this is the text users enter
        var queryString by remember { mutableStateOf("") }

        // if the search bar is active or not
        var isActive by remember { mutableStateOf(false) }

        val contextForToast = LocalContext.current.applicationContext

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isActive) 0.dp else 8.dp),
                query = queryString,
                onQueryChange = { newQueryString ->
                    queryString = newQueryString
                },
                onSearch = {
                    isActive = false
                    Toast.makeText(
                        contextForToast,
                        "Your query string: $queryString",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                },
                active = isActive,
                onActiveChange = { activeChange ->
                    isActive = activeChange
                },
                placeholder = {
                    Text(text = "Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            ) {

            }

            ScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Favorite routes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        LazyColumn {
            items(5) { message ->
                FlightCard(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    favoriteOrNot = false
                )
            }
        }
    }
}

@Composable
fun FlightCard(
    modifier: Modifier = Modifier,
    favoriteOrNot: Boolean
) {
    Card(
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 16.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "DEPART")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("IADA")
                            }
                            append("  ")
                            append("The name of the depart airport")
                        }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "ARRIVE")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("IADA")
                            }
                            append("  ")
                            append("The name of the arrive airport")
                        }
                )
            }
            Icon(
                Icons.Default.Star,
                tint = if (favoriteOrNot) Color(255, 193, 7, 255) else Color.Gray,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FlightSearchTheme {
        MainScreen()
    }
}

@Preview
@Composable
fun FavoriteFlightCardPreview() {
    FlightSearchTheme {
        FlightCard(favoriteOrNot = true)
    }
}
