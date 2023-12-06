package com.example.flightsearch.ui.screen

import androidx.lifecycle.ViewModel
import com.example.flightsearch.data.FlightDao

class FlightViewModel(
    private val databaseDao: FlightDao
) : ViewModel() {

}