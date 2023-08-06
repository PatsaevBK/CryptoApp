package com.example.cryptoapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.R
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.usecases.GetCoinInfoListUseCase
import com.example.cryptoapp.domain.usecases.GetCoinInfoUseCase
import com.example.cryptoapp.domain.usecases.LoadDataUseCase
import javax.inject.Inject

class CoinViewModel @Inject constructor(
    private val getCoinInfoUseCase: GetCoinInfoUseCase,
    private val getCoinInfoListUseCase: GetCoinInfoListUseCase,
    private val loadDataUseCase: LoadDataUseCase
) : ViewModel() {

    private val defaultList = getCoinInfoListUseCase()

    private val _coinInfoList = MutableLiveData<List<CoinInfo>>()
    val coinInfoList: LiveData<List<CoinInfo>>
        get() = _coinInfoList

    init {
        loadDataUseCase()
    }


    fun getDetailInfo(fSym: String): LiveData<CoinInfo> = getCoinInfoUseCase(fSym)

    fun makeSort(menuItemId: Int) {
        when (menuItemId) {
            R.id.sortAz -> makeSortAz()
            R.id.sortPrice -> makeSortPrice()
            R.id.sortLastUpdate -> makeSortLastUpdate()
        }
    }

    private fun makeSortLastUpdate() {
        _coinInfoList.value = defaultList.value
    }

    private fun makeSortPrice() {
        TODO("Not yet implemented")
    }

    private fun makeSortAz() {
        TODO("Not yet implemented")
    }
}