package com.dertefter.neticlient

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WearCommunicationClient @Inject constructor(@ApplicationContext context: Context) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun sendData(key: String, value: String) {
        try {
            val request = PutDataMapRequest.create("/shared_data").apply {
                dataMap.putString(key, value)
                dataMap.putLong("timestamp", System.currentTimeMillis())
            }
            val putDataRequest = request.asPutDataRequest()
            putDataRequest.setUrgent()
            dataClient.putDataItem(putDataRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}