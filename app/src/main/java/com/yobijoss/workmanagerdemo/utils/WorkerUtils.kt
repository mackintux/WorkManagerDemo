package com.yobijoss.workmanagerdemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap, name: String): Uri {

    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)

    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    val outputFile = File(outputDir, String.format("compressed-output-%s.jpg", name))

    var out: FileOutputStream? = null

    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }

        }
    }
    return Uri.fromFile(outputFile)
}