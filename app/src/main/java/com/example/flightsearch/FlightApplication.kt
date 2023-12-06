package com.example.flightsearch

import android.app.Application
import com.example.flightsearch.data.FlightDatabase

class FlightApplication : Application() {
    val databaseDao = FlightDatabase.getDatabase(this).FlightDao()
}