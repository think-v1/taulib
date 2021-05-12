package com.tauari.taulib.helper

import com.tauari.taulib.AudioFormat
import com.tauari.taulib.MediaType
import com.tauari.taulib.VideoFormat

object MediaTypeHelper {
    fun whichTypeOf(fileExt: String): String {
        if(isVideo(fileExt)) {
            return MediaType.video.name
        }
        else {
            return MediaType.audio.name
        }

    }

    fun isVideo(fileExt: String): Boolean {
        val foundList = VideoFormat.values().filter {
            fileExt == ".${MediaFormatHelper.getDisplayNameOfFormat(it.name)}"
        }
        return foundList.isNotEmpty()
    }

    fun isAudio(fileExt: String): Boolean {
        val foundList = AudioFormat.values().filter {
            fileExt == ".${MediaFormatHelper.getDisplayNameOfFormat(it.name)}"
        }
        return foundList.isNotEmpty()
    }
}