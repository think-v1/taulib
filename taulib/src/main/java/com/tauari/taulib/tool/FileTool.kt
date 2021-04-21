package com.tauari.taulib.tool

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import java.io.File
import kotlin.random.Random

object FileTool {
    val max = 10000
    val defaultName = "tauari_app_data_item"
    fun isExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    fun scanContentUriFromPaths(context: Context, paths: Array<String>, completeCallback: MediaScannerConnection.OnScanCompletedListener) {
        MediaScannerConnection.scanFile(context, paths, null, completeCallback)
    }

    fun getImageInfo(context: Context, uri: Uri): Bundle? {
        context.contentResolver.query(uri, null, null, null, null)?.use {
                cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            val result = Bundle()
            cursor.moveToFirst()
            result.putString(OpenableColumns.DISPLAY_NAME, cursor.getString(nameIndex))
            result.putLong(OpenableColumns.SIZE, cursor.getLong(sizeIndex))
            return result
        }
        return null
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    fun getNameFromPath(path: String): String {
        val lastSlashIndex = path.lastIndexOf('/')
        return path.subSequence(lastSlashIndex + 1, path.lastIndex+1).toString()
    }

    fun getNameWithExt(name: String, format: Bitmap.CompressFormat): String {
        val ext = when(format) {
            Bitmap.CompressFormat.JPEG -> ".jpg"
            Bitmap.CompressFormat.PNG -> ".png"
            else -> ".jpg"
        }

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            ext = when(format) {
//                Bitmap.CompressFormat.WEBP_LOSSLESS -> ".webp"
//                Bitmap.CompressFormat.WEBP_LOSSY -> ".webp"
//                else -> ext
//            }
//        }
        return name + ext
    }

    fun removeExtFromName(name: String): String {
        val index = name.lastIndexOf('.')
        return name.subSequence(0, index).toString()
    }

    fun getExtOfFile(name: String) : String {
        val index = name.lastIndexOf('.')
        return name.subSequence(index, name.lastIndex+1).toString()
    }

    fun findGoodNameForNewFile(dir: File, name: String) : String {
        val file = File(dir, name)
        val ext = getExtOfFile(name)
        val nameWithoutExt = removeExtFromName(name)
        if(!file.exists()) {
            return name
        }
        else {
            for(i in 1 until max) {
                val newNameWithExt = "${nameWithoutExt}_$i$ext"
                val newFile = File(dir, newNameWithExt)
                if(!newFile.exists()) {
                    return newNameWithExt
                }
            }
            return "${getRandomName()}$ext}"
        }
    }

    fun getRandomName(): String {
        return "${defaultName}_${Random(1000).nextInt()}"
    }

    fun getDisplayPath(dir: File?): String {
        return if(dir == null) {
            ""
        }
        else {
            val path = dir.path
            getDisplayPath(path)
        }
    }

    fun getDisplayPath(path: String): String {
        val text = "/storage/emulated/0/"
        return path.replace(text, "Internal Storage > ")
//        var startIndex = path.indexOf("Android")
//        if(startIndex == -1) {
//            startIndex = path.indexOf(Environment.DIRECTORY_PICTURES)
//        }
//        return "Internal Storage: /${path.subSequence(startIndex, path.length)}"
    }

    fun scanOutputDir(context: Context, dirPath: String?) {
        dirPath?.let {
            MediaScannerConnection.scanFile(context, arrayOf(it), null, null)
        }

    }
}