package com.tauari.taulib.tool.uri

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.tauari.taulib.tool.UriTool.getDataColumn

object DownloadsDocUriPathLowerMHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val id = DocumentsContract.getDocumentId(uri)
        if (id.startsWith("raw:")) {
            return id.replaceFirst("raw:".toRegex(), "")
        }
        var contentUri: Uri? =null
        try {
            contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),id.toLong())
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        contentUri?.let {
            return getDataColumn(context, it, null, null)
        }
        return null
    }
}