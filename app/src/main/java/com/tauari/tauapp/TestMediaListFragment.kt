package com.tauari.tauapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tauari.taulib.BundleKey
import com.tauari.taulib.data.model.MediaItem
import com.tauari.taulib.tool.UriTool
import com.tauari.taulib.ui.fragment.MediaListFragment


class TestMediaListFragment : MediaListFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getItemMimeType(): String {
        return "audio/*"
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            TestMediaListFragment().apply {}
    }

    override fun gotoNextFragment() {
        val data = getItemsReadyToProcess()
        val items: ArrayList<MediaItem> = data.getParcelableArrayList(BundleKey.MEDIAS_READY_TO_PROCESS) ?: arrayListOf()
        val realPath = UriTool.getPathFromUri(requireContext(), items[0].uriContent)
        Log.e("TEST", "real path: $realPath")
    }
}