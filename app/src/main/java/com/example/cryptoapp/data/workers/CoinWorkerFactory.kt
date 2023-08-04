package com.example.cryptoapp.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class CoinWorkerFactory @Inject constructor(
    private val childWorkerFactoryProviders: @JvmSuppressWildcards Map<Class<out ListenableWorker>, Provider<ChildWorkerFactory>>
) : androidx.work.WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            RefreshDataWorker::class.qualifiedName -> {
                val childWorkerFactory =
                    childWorkerFactoryProviders[RefreshDataWorker::class.java]?.get()
                return childWorkerFactory?.create(appContext, workerParameters)
            }
            else -> null
        }
    }
}