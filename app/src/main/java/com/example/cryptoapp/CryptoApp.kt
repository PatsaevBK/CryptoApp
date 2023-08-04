package com.example.cryptoapp

import android.app.Application
import androidx.work.Configuration
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.data.workers.WorkerFactory
import com.example.cryptoapp.di.DaggerApplicationComponent

class CryptoApp: Application(), Configuration.Provider {

    val applicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(
                WorkerFactory(
                    CoinMapper(),
                    ApiFactory.apiService,
                    AppDatabase.getInstance(this).coinPriceInfoDao()
                )
            )
            .build()
    }
}