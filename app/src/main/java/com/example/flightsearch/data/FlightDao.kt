package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Insert
    suspend fun insert(favorite: Favorite)
    @Delete
    suspend fun delete(favorite: Favorite)
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>
    @Query("SELECT * FROM airport ORDER BY passengers ASC")
    fun getAllAirports(): Flow<List<Airport>>
}