package com.example.cryptoapp

import android.app.Application
import androidx.work.Configuration
import com.example.cryptoapp.data.workers.CoinWorkerFactory
import com.example.cryptoapp.di.DaggerApplicationComponent
import javax.inject.Inject

class CryptoApp: Application(), Configuration.Provider {

    @Inject
    lateinit var coinWorkerFactory: CoinWorkerFactory

    val applicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        applicationComponent.inject(this)
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(coinWorkerFactory)
            .build()
    }
}