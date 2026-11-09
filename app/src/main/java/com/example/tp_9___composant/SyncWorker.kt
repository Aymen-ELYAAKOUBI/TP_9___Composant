package com.example.tp_9___composant

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log

class SyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("SyncWorker", "Background synchronization started...")
        try {
            // Simulate background data sync (e.g. uploading workouts to server)
            Thread.sleep(2000)
            Log.d("SyncWorker", "Background synchronization completed successfully.")
            return Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Background synchronization failed", e)
            return Result.retry()
        }
    }
}
