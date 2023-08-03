package com.example.cryptoapp.domain.entities

import javax.inject.Inject

data class CoinInfo @Inject constructor(
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val lastUpdate: String,
    val highDay: String?,
    val lowDay: String?,
    val lastMarket: String?,
    val imageUrl: String
)
