package com.example.cryptoapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
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

    val coinInfoList = MediatorLiveData<List<CoinInfo>>()


    init {
        loadDataUseCase()
    }


    fun getDetailInfo(fSym: String): LiveData<CoinInfo> = getCoinInfoUseCase(fSym)

    fun makeSort(menuItemId: String) {
        coinInfoList.apply {
            addSource(getCoinInfoListUseCase()) { coinInfoList ->
                val result = when(menuItemId) {
                    FILTER_PRICE -> coinInfoList.sortedBy { it.price?.toDouble() }.reversed()
                    FILTER_LAST_UPDATE -> coinInfoList
                    FILTER_A_Z -> coinInfoList.sortedBy { it.fromSymbol }
                    else -> throw RuntimeException("unknown menuItem: $menuItemId")
                }
                value = result
            }
        }
    }

    companion object {
        const val FILTER_A_Z = "a-z"
        const val FILTER_LAST_UPDATE = "lastUpdate"
        const val FILTER_PRICE = "price"
    }
}