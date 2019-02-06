package com.yobijoss.workmanagerdemo.io

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.yobijoss.workmanagerdemo.utils.KEY_UPLOAD_URI

class UploadWork(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val storage = FirebaseStorage.getInstance()

        val uri = Uri.parse(inputData.getString(KEY_UPLOAD_URI))
        val compressImageReference = storage.reference.child("images/${uri.lastPathSegment}")

        val uploadTask = compressImageReference.putFile(uri)

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