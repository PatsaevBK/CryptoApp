package com.example.cryptoapp.presentation.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.usecases.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinViewModel @Inject constructor(
    private val getCoinInfoUseCase: GetCoinInfoUseCase,
    private val getCoinInfoListLastUpdateUseCase: GetCoinInfoListLastUpdateUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val getCoinInfoListAzUseCase: GetCoinInfoListAzUseCase,
    private val getCoinInfoListPriceUseCase: GetCoinInfoListPriceUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _coinInfoList = MutableLiveData<List<CoinInfo>>()
    val coinInfoList: LiveData<List<CoinInfo>>
        get() = _coinInfoList


    init {
        loadDataUseCase()
        makeSortLastUpdate()
    }


    fun getDetailInfo(fSym: String): Flow<CoinInfo> = getCoinInfoUseCase(fSym)

    fun makeSortLastUpdate() {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            getCoinInfoListLastUpdateUseCase.invoke().collect {
                Log.d("CoinViewModel", "Flow Last Update: $it")
                _coinInfoList.value = it
            }
        }
    }

    fun makeSortAZ() {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            getCoinInfoListAzUseCase.invoke().collect {
                Log.d("CoinViewModel", "Flow Last A-Z: $it")
                _coinInfoList.value = it
            }
        }
    }

    fun makeSortPrice() {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            getCoinInfoListPriceUseCase.invoke().collect {
                Log.d("CoinViewModel", "Flow Last Price: $it")
                _coinInfoList.value = it
            }
        }
    }

    fun makeSearch(fSym: String) {
        viewModelScope.launch {
            searchUseCase.invoke(fSym).collect {
                _coinInfoList.value = it
            }
        }
    }

    fun refreshData() {
        loadDataUseCase()
    }
}