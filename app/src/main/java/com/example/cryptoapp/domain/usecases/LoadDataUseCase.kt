package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.repository.Repository

class LoadDataUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke() = repository.loadData()
}