package com.tauari.taulib.helper

object MediaFormatHelper {
    fun getDisplayNameOfFormat(rawEnumFormat: String): String {
        return if(rawEnumFormat.indexOf('_') == 0) {
            rawEnumFormat.subSequence(1, rawEnumFormat.length - 1).toString()
        } else {
            rawEnumFormat
        }
    }
}