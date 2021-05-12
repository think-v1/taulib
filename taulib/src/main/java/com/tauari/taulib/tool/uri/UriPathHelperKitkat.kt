package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.Log
import androidx.annotation.RequiresApi

object UriPathHelperKitkat {
    val tag = "UriPathHelperKitkat"
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        return when {
            DocumentsContract.isDocumentUri(context, uri) -> {
                Log.e(tag,"DocumentsContract ${uri.path}")
                DocumentUriPathHelper.getPathFromUri(context, uri)
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                Log.e(tag,"content ${uri.path}")
                ContentUriPathHelper.getPathFromUri(context, uri)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                Log.e(tag,"file ${uri.path}")
                FileUriPathHelper.getPathFromUri(uri)
            }
            else -> {
                Log.e(tag,"null ${uri.path}")
                uri.path
            }
        }
    }
}