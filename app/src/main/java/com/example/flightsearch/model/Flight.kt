package com.example.flightsearch.model


data class Flight(
    val departureCode: String,
    val departureAirportName: String,
    val destinationCode: String,
    val destinationAirportName: String,
)
