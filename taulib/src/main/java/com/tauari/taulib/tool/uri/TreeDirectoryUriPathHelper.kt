package com.tauari.taulib.tool.uri

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.File


object TreeDirectoryUriPathHelper {
    const val PATH_TREE = "tree";
    val PRIMARY_TYPE = "primary";
    val RAW_TYPE = "raw";
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getPathFromUri(context: Context, uri: Uri): String? {
        if ("file" == uri.scheme) {
            return uri.path
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && isTreeUri(uri)
        ) {
            val treeId = getTreeDocumentId(uri)
            if (treeId != null) {
                Log.e("TREE","TreeId -> $treeId",)
                val paths = treeId.split(":").toTypedArray()
                val type = paths[0]
                val subPath = if (paths.size == 2) paths[1] else ""
                return when {
                    RAW_TYPE.equals(type, ignoreCase = true) -> {
                        treeId.substring(treeId.indexOf(File.separator))
                    }
                    PRIMARY_TYPE.equals(type, ignoreCase = true) -> {
                        "${Environment.getExternalStorageDirectory()}${File.separator}$subPath"
                    }
                    else -> {
                        val path = StringBuilder()
                        val pathSegment = treeId.split(":").toTypedArray()
                        if (pathSegment.size == 1) {
                            path.append(getRemovableStorageRootPath(context, paths[0]))
                        } else {
                            val rootPath: String? = getRemovableStorageRootPath(context, paths[0])
                            path.append(rootPath).append(File.separator).append(pathSegment[1])
                        }
                        path.toString()
                    }
                }
            }
        }
        return null
    }

    private fun getRemovableStorageRootPath(context: Context, storageId: String): String? {
        val rootPath = java.lang.StringBuilder()
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
        for (fileDir in externalFilesDirs) {
            if (fileDir.path.contains(storageId)) {
                val pathSegment = fileDir.path.split(File.separator).toTypedArray()
                for (segment in pathSegment) {
                    if (segment == storageId) {
                        rootPath.append(storageId)
                        break
                    }
                    rootPath.append(segment).append(File.separator)
                }
                //rootPath.append(fileDir.getPath().split("/Android")[0]); // faster
                break
            }
        }
        return rootPath.toString()
    }

    fun getTreeDocumentId(uri: Uri): String? {
        val paths = uri.pathSegments
        return if (paths.size >= 2 && PATH_TREE == paths[0]) {
            paths[1]
        } else null
    }

    fun isTreeUri(uri: Uri): Boolean {
        val paths = uri.pathSegments
        return paths.size == 2 && PATH_TREE == paths[0]
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getListRemovableStorage(context: Context?): List<String>? {
        val paths: MutableList<String> = ArrayList()
        val externalFilesDirs = ContextCompat.getExternalFilesDirs(context!!, null)
        for (fileDir in externalFilesDirs) {
            if (Environment.isExternalStorageRemovable(fileDir)) {
//                Timber.d("ext-raw-path -> %s", fileDir.absolutePath)
                val path = fileDir.path
                if (path.contains("/Android")) {
                    paths.add(path.substring(0, path.indexOf("/Android")))
                }
            }
        }
        return paths
    }

}