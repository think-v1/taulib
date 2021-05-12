package com.tauari.taulib.tool.uri

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.tauari.taulib.tool.UriTool.getDataColumn

object DownloadsDocUriPathMHelper {
    @RequiresApi(Build.VERSION_CODES.M)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val fileName = cursor.getString(0)
                val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                if (!TextUtils.isEmpty(path)) {
                    return path
                }
            }
        } finally {
            cursor?.close()
        }
        val id: String = DocumentsContract.getDocumentId(uri)
        if (!TextUtils.isEmpty(id)) {
            if (id.startsWith("raw:")) {
                return id.replaceFirst("raw:".toRegex(), "")
            }
            val contentUriPrefixesToTry = arrayOf(
                "content://downloads/public_downloads",
                "content://downloads/my_downloads"
            )
            for (contentUriPrefix in contentUriPrefixesToTry) {
                return try {
                    val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), java.lang.Long.valueOf(id))
                    getDataColumn(context, contentUri, null, null)
                } catch (e: NumberFormatException) {
                    //In Android 8 and Android P the id is not a number
                    uri.path!!.replaceFirst("^/document/raw:".toRegex(), "").replaceFirst("^raw:".toRegex(), "")
                }
            }
        }
        return null
    }
}