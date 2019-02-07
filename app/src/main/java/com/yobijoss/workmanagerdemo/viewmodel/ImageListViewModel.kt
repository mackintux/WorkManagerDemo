package com.yobijoss.workmanagerdemo.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.yobijoss.workmanagerdemo.io.CleanupWorker
import com.yobijoss.workmanagerdemo.io.UploadWork
import com.yobijoss.workmanagerdemo.utils.KEY_UPLOAD_URI
import com.yobijoss.workmanagerdemo.utils.TAG_UPLOAD

class ImageListViewModel : ViewModel() {

    private val workManager: WorkManager = WorkManager.getInstance()

    val outputWorkInfos: LiveData<List<WorkInfo>>

    val uriListLiveData: MutableLiveData<ArrayList<Uri>> = MutableLiveData()

    init {
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_UPLOAD)
    }

    fun addUri(uri: Uri) {
        var uriList = uriListLiveData.value

        uriList?.add(uri) ?: run {
            uriList = arrayListOf(uri)
        }

        uriListLiveData.value = uriList
    }

    fun syncImages() {

        val uploadConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        uriListLiveData.value?.forEach {

            val cleanUpWork = OneTimeWorkRequest.Builder(CleanupWorker::class.java)
                .build()


            val uploadWork = OneTimeWorkRequest.Builder(UploadWork::class.java)
                .setInputData(createInputDataForUri(it))
                .setConstraints(uploadConstraints)
                .addTag(TAG_UPLOAD)
                .build()

           workManager
               .beginWith(cleanUpWork)
               .then(uploadWork)
               .enqueue()
        }
    }

    private fun createInputDataForUri(uri: Uri): Data {
        val builder = Data.Builder()

        uriListLiveData.value?.let {
            builder.putString(KEY_UPLOAD_URI, uri.toString()) //todo en el siguiente work  cambia el key a compress
        }

        return builder.build()
    }

}