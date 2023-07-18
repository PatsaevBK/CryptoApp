package com.example.cryptoapp.domain

class GetCoinInfoListUseCase(
    private val repository: Repository
) {
    operator fun invoke() = repository.getCoinInfoList()
}