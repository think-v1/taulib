package com.tauari.taulib.tool.uri

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object UriPathHelperLowerKitkat {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            if(cursor!=null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)
            }
            else {
                null
            }
        } finally {
            cursor?.close()
        }
    }
}