package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

object DownloadsDocUriPathHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DownloadsDocUriPathMHelper.getPathFromUri(context, uri)
        }
        else {
            DownloadsDocUriPathLowerMHelper.getPathFromUri(context, uri)
        }
    }
}