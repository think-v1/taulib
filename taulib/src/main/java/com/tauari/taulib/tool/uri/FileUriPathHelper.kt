package com.tauari.taulib.tool.uri

import android.net.Uri

object FileUriPathHelper {
    fun getPathFromUri(uri: Uri): String? {
        return uri.path
    }
}