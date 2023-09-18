package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(fromSymbol: String): Flow<List<CoinInfo>> {
        return repository.searchCoin(fromSymbol)
    }
}