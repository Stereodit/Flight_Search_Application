package com.example.flightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearch.data.FlightDatabase
import com.example.flightsearch.data.UserPreferencesRepository

private const val SEARCHBAR_TEXT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCHBAR_TEXT_PREFERENCE_NAME
)

class FlightApplication : Application() {
    val database : FlightDatabase by lazy { FlightDatabase.getDatabase(this) }
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}