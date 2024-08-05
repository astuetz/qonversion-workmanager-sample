package com.astuetz.qonversionworkmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.offerings.QOfferings
import com.qonversion.android.sdk.listeners.QonversionOfferingsCallback
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

@HiltWorker
class GetOfferingsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("astuetz", "GetOfferingsWorker::doWork")

        when (val offeringsResult = getOfferings()) {
            is QonversionOfferingsResult.Success -> {
                Log.d("astuetz", "getOfferings success, main offering: ${offeringsResult.offerings.main}")
            }
            is QonversionOfferingsResult.Error -> {
                Log.d("astuetz", "getOfferings error: ${offeringsResult.error}")
            }
        }

        return Result.success()
    }

    companion object {

        fun schedule(
            context: Context,
            timestamp: Long,
        ) {
            val initialDelay = timestamp - System.currentTimeMillis()

            val subscriptionUploadWork = OneTimeWorkRequest.Builder(GetOfferingsWorker::class.java).apply {
                setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            }

            Log.d("astuetz", "Enqueuing GetOfferingsWorker for $timestamp")

            WorkManager.getInstance(context).enqueue(subscriptionUploadWork.build())
        }

    }
}

suspend fun getOfferings(): QonversionOfferingsResult = suspendCancellableCoroutine { continuation ->
    Log.d("astuetz", "Calling Qonversion.shared.offerings")
    Qonversion.shared.offerings(
        object : QonversionOfferingsCallback {
            override fun onSuccess(offerings: QOfferings) {
                Log.d("astuetz", "QonversionOfferingsCallback::onSuccess")
                if (continuation.isActive) {
                    continuation.resume(QonversionOfferingsResult.Success(offerings))
                }
            }

            override fun onError(error: QonversionError) {
                Log.d("astuetz", "QonversionOfferingsCallback::onError")
                if (continuation.isActive) {
                    continuation.resume(QonversionOfferingsResult.Error(error))
                }
            }
        },
    )
}


sealed interface QonversionOfferingsResult {
    data class Success(val offerings: QOfferings) : QonversionOfferingsResult
    data class Error(val error: QonversionError) : QonversionOfferingsResult
}
