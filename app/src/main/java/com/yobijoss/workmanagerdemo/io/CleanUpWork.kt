package com.yobijoss.workmanagerdemo.io

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yobijoss.workmanagerdemo.utils.OUTPUT_PATH
import com.yobijoss.workmanagerdemo.utils.TAG
import java.io.File


class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {


    override fun doWork(): Result {

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty()) {
                            val deleted = entry.delete()
                            Log.i(TAG, String.format("Deleted %s - %s", name, deleted))
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)
            Result.failure()
        }
    }
}
