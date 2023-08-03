package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.workers.RefreshDataWorkers
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val application: Application,
    private val mapper: CoinMapper
) : Repository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()



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

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorkers.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorkers.makeRequest()
        )
    }
}