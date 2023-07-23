package com.example.cryptoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.repository.RepositoryImpl
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.usecases.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase
import kotlinx.coroutines.*

class CoinViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = RepositoryImpl(application)

    private val getCoinInfoUseCase = GetCoinInfoUseCase(repository)
    private val getCoinInfoListUseCase = GetCoinInfoListUseCase(repository)
    private val loadDataUseCase = LoadDataUseCase(repository)

    init {
        viewModelScope.launch {
            loadDataUseCase()
        }
    }

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String): LiveData<CoinInfo> = getCoinInfoUseCase(fSym)

}