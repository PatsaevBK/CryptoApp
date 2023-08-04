package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay

class RefreshDataWorkers(
    context: Context,
    workerParameters: WorkerParameters,
    private val mapper: CoinMapper,
    private val apiService: ApiService,
    private val coinInfoDao: CoinInfoDao
): CoroutineWorker(context, workerParameters) {

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