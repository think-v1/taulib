package com.tauari.taulib.ui.fragment

import android.content.ClipData
import android.net.Uri

interface OnMediaAddition {
    fun isAllowToAdd(data: Uri): Boolean
    fun isAllowToAdd(data: ClipData): Boolean
    fun executeAdd(data: Uri)
    fun executeAdd(data: ClipData)
}