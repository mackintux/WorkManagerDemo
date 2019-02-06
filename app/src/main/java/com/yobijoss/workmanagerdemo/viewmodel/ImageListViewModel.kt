package com.yobijoss.workmanagerdemo.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.yobijoss.workmanagerdemo.io.CompressWork
import com.yobijoss.workmanagerdemo.io.UploadWork
import com.yobijoss.workmanagerdemo.utils.KEY_COMPRESS_URI

class ImageListViewModel : ViewModel() {

    private val workManager: WorkManager = WorkManager.getInstance()

    val uriListLiveData: MutableLiveData<ArrayList<Uri>> = MutableLiveData()

    fun addUri(uri: Uri) {
        var uriList = uriListLiveData.value

        uriList?.add(uri) ?: run {
            uriList = arrayListOf(uri)
        }

        uriListLiveData.value = uriList
    }

    fun syncImages() {
        val compressConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val uploadConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        uriListLiveData.value?.forEach {

            val compressWork = OneTimeWorkRequest.Builder(CompressWork::class.java)
                .setInputData(createInputDataForUri(it))
                .setConstraints(compressConstraints)
                .build()


            val uploadWork = OneTimeWorkRequest.Builder(UploadWork::class.java)
                .setConstraints(uploadConstraints)
                .build()

           workManager
               .beginWith(compressWork)
               .then(uploadWork)
               .enqueue()
        }
    }

    private fun createInputDataForUri(uri: Uri): Data {
        val builder = Data.Builder()

        uriListLiveData.value?.let {
            builder.putString(KEY_COMPRESS_URI, uri.toString())
        }

        return builder.build()
    }

}