package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object GoogleDriveUriPathHelper {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val returnCursor = context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null) ?: return null

        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)

//        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.cacheDir, name)
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
//            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
//            Log.e("File Path", "Path " + file.path)
//            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            returnCursor.close()
        }
        return file.path
    }
}