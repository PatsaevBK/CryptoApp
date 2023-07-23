package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.repository.Repository

class GetCoinInfoUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke(fromSymbol: String) = repository.getCoinInfo(fromSymbol)
}