package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.tauari.taulib.tool.UriTool.getDataColumn

object MediaDocUriPathHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val type = split[0]
        var contentUri: Uri? = null
        when (type) {
            "image" -> {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            "video" -> {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            "audio" -> {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        }
        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])
        return contentUri?.let {
            getDataColumn(context, it, selection,
                selectionArgs)
        }
    }
}