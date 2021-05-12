package com.tauari.taulib.tool.uri

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.annotation.RequiresApi
import com.tauari.taulib.tool.FileTool

object ExternalStorageUriPathHelper {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPathFromUri(uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val type = split[0]
        val fullPath = getPathFromExtSD(split)
        return if (fullPath !== "") {
            fullPath
        } else {
            null
        }
    }

    @Throws(SecurityException::class)
    private fun getPathFromExtSD(pathData: Array<String>): String? {
        val type = pathData[0]
        val relativePath = "/" + pathData[1]
        var fullPath = ""
        if ("primary".equals(type, ignoreCase = true)) {
            fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
            if (FileTool.isExists(fullPath)) {
                return fullPath
            }
        }
        fullPath = System.getenv("SECONDARY_STORAGE")?:"" + relativePath
        if (FileTool.isExists(fullPath)) {
            return fullPath
        }
        fullPath = System.getenv("EXTERNAL_STORAGE")?:"" + relativePath
        return if (FileTool.isExists(fullPath)) {
            fullPath
        } else fullPath
    }
}