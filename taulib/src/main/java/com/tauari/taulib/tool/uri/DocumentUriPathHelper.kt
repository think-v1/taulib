package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.tauari.taulib.tool.UriTool.isDownloadsDocument
import com.tauari.taulib.tool.UriTool.isExternalStorageDocument
import com.tauari.taulib.tool.UriTool.isGoogleDriveUri
import com.tauari.taulib.tool.UriTool.isMediaDocument

object DocumentUriPathHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        return when {
            isExternalStorageDocument(uri) -> {
                ExternalStorageUriPathHelper.getPathFromUri(uri)
            }
            isDownloadsDocument(uri) -> {
                DownloadsDocUriPathHelper.getPathFromUri(context, uri)
            }
            isMediaDocument(uri) -> {
                MediaDocUriPathHelper.getPathFromUri(context, uri)
            }
            isGoogleDriveUri(uri) -> {
                GoogleDriveUriPathHelper.getPathFromUri(context, uri)
            }
            else -> null
        }
    }
}