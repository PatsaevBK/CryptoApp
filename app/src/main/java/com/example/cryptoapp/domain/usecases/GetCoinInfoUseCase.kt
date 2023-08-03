package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.repository.Repository
import javax.inject.Inject

class GetCoinInfoUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke(fromSymbol: String) = repository.getCoinInfo(fromSymbol)
}