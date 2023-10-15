package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.*
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val mapper: CoinMapper,
    private val apiService: ApiService,
    private val coinInfoDao: CoinInfoDao
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            val topCoins = apiService.getTopCoinsInfo(limit = 50)
            val stringNamesTopCoins = mapper.mapNamesListToString(topCoins)
            val jsonContainer = apiService.getFullPriceList(fSyms = stringNamesTopCoins)
            val listCoinInfoDto = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
            coinInfoDao.insertPriceList(listCoinInfoDto.map { mapper.mapDtoToDbModel(it) })
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        const val NAME = "refreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<RefreshDataWorker>()
                .build()
        }
    }

    class Factory @Inject constructor(
        private val mapper: CoinMapper,
        private val apiService: ApiService,
        private val coinInfoDao: CoinInfoDao
    ): ChildWorkerFactory {

        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return RefreshDataWorker(
                context,
                workerParameters,
                mapper,
                apiService,
                coinInfoDao
            )
        }
    }
}