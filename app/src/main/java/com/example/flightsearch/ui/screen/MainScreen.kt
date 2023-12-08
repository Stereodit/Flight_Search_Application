package com.example.flightsearch.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.model.Flight
import com.example.flightsearch.ui.theme.FlightSearchTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class FlightScreens {
    FavoriteFlights,
    SearchingFlights
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: FlightViewModel = viewModel(factory = FlightViewModel.factory)
) {
    val navController = rememberNavController()
    val uiState = viewModel.uiState.collectAsState()
    val uiStatePreferences = viewModel.uiStatePreferences.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

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

        var queryString by rememberSaveable { mutableStateOf("") }
        var isActive by rememberSaveable { mutableStateOf(false) }

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
                    viewModel.saveSearchBar(queryString)
                    navController.navigate(FlightScreens.SearchingFlights.name)
                },
                active = isActive,
                onActiveChange = { activeChange ->
                    isActive = activeChange
                },
                placeholder = {
                    Text(text = if(uiStatePreferences.searchString != "") "Are you looking for " + uiStatePreferences.searchString + "?" else "Search")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            ) {
                viewModel.getAirportsListBySearchString(queryString).forEach { airport ->
                    ClickableText(
                        text =
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(airport.iataCode)
                                }
                                append("  ")
                                append(airport.name)
                            },
                        onClick = {
                            queryString = airport.iataCode
                            isActive = false
                            viewModel.saveSearchBar(queryString)
                            navController.navigate(FlightScreens.SearchingFlights.name)
                        },
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }
            NavHost(
                navController = navController,
                startDestination = FlightScreens.FavoriteFlights.name
            ) {
                composable(FlightScreens.FavoriteFlights.name) {
                    FavoriteScreenContent(
                        favoriteFlightsList = viewModel.convertToFlightsList(uiState.value.favoriteList),
                        onClick = {
                            coroutineScope.launch {
                                viewModel.deleteFavorite(it)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
                composable(FlightScreens.SearchingFlights.name) {
                    SearchingScreenContent(
                        searchingFlightsList = viewModel.getAllSearchingFlights(queryString),
                        searchString = queryString,
                        onClick = { favorite, isFavorite ->
                            if (!isFavorite) {
                                coroutineScope.launch {
                                    viewModel.insertFavorite(favorite)
                                }
                            } else {
                                coroutineScope.launch {
                                    viewModel.deleteFavorite(favorite)
                                }
                            }
                        },
                        onBackButtonClick = {
                            navController.navigate(FlightScreens.FavoriteFlights.name)
                            queryString = ""
                            isActive = false
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoriteFlightCardPreview() {
    FlightSearchTheme {
        FavoriteFlightCard(
            flight = Flight(
                "ARN",
                "Stockholm Arlanda Airport",
                "WAW",
                "Warsaw Chopin Airport"),
            {}
        )
    }
}
