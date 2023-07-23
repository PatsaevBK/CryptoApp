package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.repository.Repository
import kotlinx.coroutines.delay

class RepositoryImpl(
    private val application: Application
) : Repository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    private val apiService = ApiFactory.apiService

    private val mapper = CoinMapper()


    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return coinInfoDao.getPriceList().map { coinInfoDbModelList ->
            coinInfoDbModelList.map {
                mapper.mapCoinInfoDbModelToCoinInfo(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return coinInfoDao.getPriceInfoAboutCoin(fromSymbol)
            .map { mapper.mapCoinInfoDbModelToCoinInfo(it) }
    }

    override suspend fun loadData() {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val stringNamesTopCoins = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = stringNamesTopCoins)
                val listCoinInfoDto = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                coinInfoDao.insertPriceList(listCoinInfoDto.map { mapper.mapDtoToDbModel(it) })

            } catch (e: Exception) {

            }
            delay(10000)
        }
    }
}