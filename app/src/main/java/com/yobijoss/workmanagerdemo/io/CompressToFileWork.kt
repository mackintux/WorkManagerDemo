package com.yobijoss.workmanagerdemo.io

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yobijoss.workmanagerdemo.utils.KEY_UPLOAD_URI
import com.yobijoss.workmanagerdemo.utils.KEY_COMPRESS_URI
import com.yobijoss.workmanagerdemo.utils.writeBitmapToFile

class CompressToFileWork(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val TAG by lazy { CompressToFileWork::class.java.simpleName }

    override fun doWork(): Result {
        val uri = Uri.parse(inputData.getString(KEY_COMPRESS_URI))

        val resolver = applicationContext.contentResolver
        val image = BitmapFactory.decodeStream(resolver.openInputStream(uri))

        Log.d(TAG,"Compressing ${uri.lastPathSegment}")

        val outputUri = writeBitmapToFile(context, image, uri.lastPathSegment)

        val outputData = Data.Builder()
            .putString(KEY_UPLOAD_URI, outputUri.toString())
            .build()

        return Result.success(outputData)
    }

}