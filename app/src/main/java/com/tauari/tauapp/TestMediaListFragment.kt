package com.tauari.tauapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tauari.taulib.ui.fragment.MediaListFragment


class TestMediaListFragment : MediaListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getItemMimeType(): String {
        return "audio/*,video/*"
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            TestMediaListFragment().apply {}
    }
}