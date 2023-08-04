package com.example.cryptoapp.di

import android.app.Application
import com.example.cryptoapp.CryptoApp
import com.example.cryptoapp.presentation.CoinDetailFragment
import com.example.cryptoapp.presentation.CoinPriceListActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {


    fun inject(activity: CoinPriceListActivity)

    fun inject(coinDetailFragment: CoinDetailFragment)

    fun inject(application: CryptoApp)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}