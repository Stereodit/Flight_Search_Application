package com.example.flightsearch.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.model.Flight

@Composable
fun SearchingScreenContent(
    searchingFlightsList: List<Flight>,
    searchString: String,
    onClick: (Favorite, Boolean) -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackButtonClick.invoke()
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = if (searchString != "") "Flights from \"$searchString\"" else "All available flights:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        LazyColumn {
            items(searchingFlightsList) { it ->
                SearchingFlightCard(
                    flight = it,
                    onClick = onClick,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SearchingFlightCard(
    flight: Flight,
    onClick: (Favorite, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFavorite by rememberSaveable { mutableStateOf(false) }
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
                            append(flight.departureCode)
                        }
                        append("  ")
                        append(flight.departureAirportName)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "ARRIVE")
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text =
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(flight.destinationCode)
                        }
                        append("  ")
                        append(flight.destinationAirportName)
                    }
                )
            }
            Icon(
                Icons.Default.Star,
                tint = if (isFavorite) Color(255, 193, 7, 255) else Color.Gray,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onClick(Favorite(0, flight.departureCode, flight.destinationCode), isFavorite)
                        isFavorite = !isFavorite
                    }
            )
        }

    }
}