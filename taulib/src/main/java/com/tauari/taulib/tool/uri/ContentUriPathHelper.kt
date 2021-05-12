package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import com.tauari.taulib.tool.UriTool.getDataColumn
import com.tauari.taulib.tool.UriTool.isGoogleDriveUri
import com.tauari.taulib.tool.UriTool.isGooglePhotosUri

object ContentUriPathHelper {
    fun getPathFromUri(context: Context, uri: Uri): String? {
        return when {
            isGooglePhotosUri(uri) -> {
                uri.lastPathSegment!!
            }
            isGoogleDriveUri(uri) -> {
                GoogleDriveUriPathHelper.getPathFromUri(context, uri)
            }
            else -> if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                MediaDocUriNPathHelper.getPathFromUri(context, uri)
            } else {
                getDataColumn(context, uri, null, null)
            }
        }
    }
}