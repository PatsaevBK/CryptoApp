package com.example.cryptoapp.domain

class GetCoinInfoUseCase(
    private val repository: Repository
) {

    operator fun invoke(fromSymbol: String) = repository.getCoinInfo(fromSymbol)
}