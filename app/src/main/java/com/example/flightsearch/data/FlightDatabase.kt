package com.example.flightsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favorite::class], version = 2, exportSchema = false)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun FlightDao(): FlightDao

    companion object {
        @Volatile
        private var Instance: FlightDatabase? = null

        fun getDatabase(context: Context): FlightDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    FlightDatabase::class.java,
                    "flight_database"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}