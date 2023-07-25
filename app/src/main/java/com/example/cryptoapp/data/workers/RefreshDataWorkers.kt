package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.*
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory.apiService
import kotlinx.coroutines.delay

class RefreshDataWorkers(
    context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    private val mapper = CoinMapper()
    private val coinInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()

    override suspend fun doWork(): Result {
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

    companion object {
        const val NAME = "refreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshDataWorkers>()
                .build()
        }
    }
}