package com.example.cryptoapp.domain

import androidx.lifecycle.LiveData

interface Repository {

    fun getCoinInfoList(): LiveData<List<CoinInfo>>

    fun getCoinInfo(fromSymbol: String): CoinInfo
}