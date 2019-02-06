package com.yobijoss.workmanagerdemo.io

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.yobijoss.workmanagerdemo.utils.KEY_UPLOAD_URI

class UploadWork(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val TAG by lazy { UploadWork::class.java.simpleName }

    override fun doWork(): Result {

        val storage = FirebaseStorage.getInstance()

        val uri = Uri.parse(inputData.getString(KEY_UPLOAD_URI))
        val compressedImageReference = storage.reference.child("images/${uri.lastPathSegment}")

        Log.d(TAG,"Uploading ${uri.lastPathSegment}")
        val uploadTask = compressedImageReference.putFile(uri)

        var result = Result.success()

        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            result = Result.failure()
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

        return result
    }

}