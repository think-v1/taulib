package com.tauari.taulib.tool

import android.content.SharedPreferences

object PrefTool {
    fun applyStringToPref(pref: SharedPreferences, key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun applyIntToPref(pref: SharedPreferences, key: String, value: Int) {
        pref.edit().putInt(key, value).apply()
    }
}