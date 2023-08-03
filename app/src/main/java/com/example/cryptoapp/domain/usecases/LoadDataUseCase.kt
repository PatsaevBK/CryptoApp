package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.repository.Repository
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val repository: Repository
) {

    operator fun invoke() = repository.loadData()
}