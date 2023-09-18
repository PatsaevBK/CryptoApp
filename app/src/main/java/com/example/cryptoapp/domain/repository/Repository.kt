package com.example.cryptoapp.domain.repository

import com.example.cryptoapp.domain.entities.CoinInfo
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getCoinInfoListLastUpdate(): Flow<List<CoinInfo>>

    fun getCoinInfo(fromSymbol: String): Flow<CoinInfo>

    fun loadData()

    fun getCoinInfoListAZ(): Flow<List<CoinInfo>>

    fun getCoinInfoListPrice(): Flow<List<CoinInfo>>

    fun searchCoin(fromSymbol: String): Flow<List<CoinInfo>>
}