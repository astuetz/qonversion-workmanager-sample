package com.astuetz.qonversionworkmanager

import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionConfig
import com.qonversion.android.sdk.dto.QEnvironment
import com.qonversion.android.sdk.dto.QLaunchMode
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

private const val QONVERSION_PROJECT_KEY = ""

@HiltAndroidApp
class Application : android.app.Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        initQonversion()
    }

    private fun initQonversion() {
        Log.d("astuetz", "Initializing Qonversion")
        val qonversionConfig = QonversionConfig.Builder(
            context = applicationContext,
            projectKey = QONVERSION_PROJECT_KEY,
            launchMode = QLaunchMode.SubscriptionManagement,
        ).apply {
            setEnvironment(QEnvironment.Sandbox)
        }.build()
        Qonversion.initialize(qonversionConfig)
        Log.d("astuetz", "Finished initializing Qonversion")
    }
}
