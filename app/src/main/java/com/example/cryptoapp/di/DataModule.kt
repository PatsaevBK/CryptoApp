package com.example.cryptoapp.di

import com.example.cryptoapp.data.repository.RepositoryImpl
import com.example.cryptoapp.domain.repository.Repository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository
}