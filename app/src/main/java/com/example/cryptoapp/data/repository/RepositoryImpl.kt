package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.cryptoapp.data.database.CoinInfoDao
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.workers.RefreshDataWorker
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val application: Application,
    private val coinInfoDao: CoinInfoDao,
    private val mapper: CoinMapper
) : Repository {

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
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }

    override fun getCoinInfoListAZ(): LiveData<List<CoinInfo>> {
        return coinInfoDao.getPriceListAZ().map { coinInfoDbModelsList ->
            coinInfoDbModelsList.map { mapper.mapCoinInfoDbModelToCoinInfo(it) }
        }
    }

    override fun getCoinInfoListPrice(): LiveData<List<CoinInfo>> {
        return coinInfoDao.getPriceListPrice().map { coinInfoDbModelsList ->
            coinInfoDbModelsList.map { mapper.mapCoinInfoDbModelToCoinInfo(it) }
        }
    }
}