package com.example.flightsearch.ui.screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightApplication
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightDao
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.model.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightViewModel(
    private val databaseDao: FlightDao,
    private var userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightApplication)
                FlightViewModel(application.database.FlightDao(), application.userPreferencesRepository)
            }
        }
    }

    val uiState: StateFlow<UiState> =
        databaseDao.getAllFavorites().map { UiState(it) }
           .stateIn(
               scope = viewModelScope,
               started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
               initialValue = UiState()
           )

    val uiStatePreferences: StateFlow<UiStatePreferences> =
        userPreferencesRepository.searchBarText.map { searchBarText ->
            UiStatePreferences(searchBarText)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiStatePreferences()
            )

    private lateinit var airportsList: List<Airport>

    init {
        viewModelScope.launch {
            airportsList = databaseDao.getAllAirports().first() ?: emptyList()
        }
    }

    fun saveSearchBar(searchBarText: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveSearchBarPreference(searchBarText)
        }
    }

    fun getAirportsListBySearchString(searchString: String) : List<Airport> {
        return airportsList.filter { it.iataCode.contains(searchString) || it.name.contains(searchString)}
    }

    fun convertToFlightsList(favoriteList: List<Favorite>) : List<Flight> {
        val flightList: MutableList<Flight> = mutableListOf()

        favoriteList.forEach { favorite ->
            flightList.add(
                Flight(
                    favorite.departureCode,
                    airportsList.find { it.iataCode == favorite.departureCode }!!.name,
                    favorite.destinationCode,
                    airportsList.find { it.iataCode == favorite.destinationCode }!!.name,
                )
            )
        }
        return flightList
    }

    fun getAllSearchingFlights(searchString: String) : List<Flight> {
        val flightList: MutableList<Flight> = mutableListOf()
        val airport: Airport? = airportsList.find { it.iataCode.contains(searchString) }

        if (airport != null) {
            airportsList.forEach {
                if (it != airport)
                    flightList.add(Flight(airport.iataCode, airport.name, it.iataCode, it.name))
            }
            return flightList
        } else return emptyList()
    }

    suspend fun insertFavorite(favorite: Favorite) {
        databaseDao.insertFavorite(favorite)
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        databaseDao.deleteFavorite(favorite.departureCode, favorite.destinationCode)
    }

}

data class UiState(
    val favoriteList: List<Favorite> = listOf()
)

data class UiStatePreferences(
    val searchString: String = ""
)