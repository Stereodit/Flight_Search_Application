package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)
    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
    suspend fun deleteFavorite(departureCode: String, destinationCode: String)
    @Query("SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>
    @Query("SELECT * FROM airport ORDER BY passengers ASC")
    fun getAllAirports(): Flow<List<Airport>>
    @Query("SELECT * FROM airport WHERE iata_code = :iataCode")
    fun getAirport(iataCode: String): Flow<Airport>
}