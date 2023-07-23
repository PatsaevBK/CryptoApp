package com.example.cryptoapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinInfo(
    val fromSymbol: String,
    val toSymbol: String?,
    val price: String?,
    val lastUpdate: String,
    val highDay: String?,
    val lowDay: String?,
    val lastMarket: String?,
    val imageUrl: String
): Parcelable
