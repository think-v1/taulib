package com.tauari.taulib.ui.fragment

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tauari.taulib.BundleKey
import com.tauari.taulib.R
import com.tauari.taulib.RequestCode
import com.tauari.taulib.adapter.MediaItemListAdapter
import com.tauari.taulib.ui.ListItemDivider


open class MediaListFragment : AnimatedFragment(), OnGotoNextFragment, OnMediaAddition {

    private lateinit var btnNext: FloatingActionButton
    private lateinit var btnAddPhotos: FloatingActionButton
    private lateinit var btnClearAll: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: MediaItemListAdapter
    private lateinit var txtEmpty: TextView
    private lateinit var txtNext: TextView
    private lateinit var txtClear: TextView
    private val observerImp = AdapterDataObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = MediaItemListAdapter(requireContext(), arrayListOf())
        adapter.registerAdapterDataObserver(observerImp)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        initRecyclerView()
        registerButtonClickListeners()
        updateViewsVisible()
    }

    private fun findViews() {
        btnAddPhotos = requireActivity().findViewById(R.id.fbtn_add_photos)
        btnClearAll = requireActivity().findViewById(R.id.fbtn_clear_all)
        btnNext = requireActivity().findViewById(R.id.fbtn_next)
        recyclerView = requireActivity().findViewById(R.id.recy_medias)
        txtEmpty = requireActivity().findViewById(R.id.txt_empty_photos)
        txtNext = requireActivity().findViewById(R.id.txt_next)
        txtClear = requireActivity().findViewById(R.id.txt_clear)
    }



    private fun initRecyclerView() {
        recyclerView.addItemDecoration(ListItemDivider(requireContext()))
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun registerButtonClickListeners() {
        btnNext.setOnClickListener { gotoNextFragment() }
        btnClearAll.setOnClickListener { clearAllItems() }
        btnAddPhotos.setOnClickListener {
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//                openFileMan()
//            }
//            else {
            pickFiles()
//            }
        }
    }

    override fun gotoNextFragment() {
        //Need to implement from children
    }

    fun getPhotosReadyToConvert(): Bundle {
        val data = Bundle()
        data.putParcelableArrayList(BundleKey.MEDIAS_READY_TO_CONVERT, adapter.dataSource)
        return data
    }

    private fun clearAllItems() {
        adapter.clearDataSource()
    }

    private fun pickFiles() {
        val actionPickIntent = Intent(Intent.ACTION_PICK)
        actionPickIntent.type = "image/*"
        actionPickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        actionPickIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(actionPickIntent, RequestCode.PICK_FILES_FROM_OS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == Activity.RESULT_OK && requestCode == RequestCode.PICK_FILES_FROM_CUSTOM_FILE_MAN) {
//            val selectedFiles = data?.getParcelableArrayListExtra<TFileParcelable>(FileManager.EXTRA_SELECTED_FILES)
//            if (selectedFiles != null) {
//                adapter.insertFromFiles(selectedFiles)
//            }
//        }
        if(resultCode == Activity.RESULT_OK && requestCode == RequestCode.PICK_FILES_FROM_OS) {
            data?.let {
                if(it.clipData != null) {
                    executeAdd(it.clipData!!)
                }
                else if(it.data != null) {
                    executeAdd(it.data!!)
                }
            }
        }
    }

    override fun executeAdd(data: Uri) {
        if(isAllowToAdd(data)) {
            adapter.insertFromUri(data)
        }
    }

    override fun isAllowToAdd(data: Uri): Boolean {
        return true
    }

    override fun executeAdd(data: ClipData) {
        if(isAllowToAdd(data)) {
            adapter.insertFromClipData(data)
        }
    }

    override fun isAllowToAdd(data: ClipData): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.unregisterAdapterDataObserver(observerImp)
//        Log.e(tag, "destroy, count=${adapter.itemCount}")
    }

    inner class AdapterDataObserver: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            updateViewsVisible()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            updateViewsVisible()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            updateViewsVisible()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            updateViewsVisible()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            super.onItemRangeChanged(positionStart, itemCount, payload)
            updateViewsVisible()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            updateViewsVisible()
        }
    }

    private fun updateViewsVisible() {
        updateTxtEmptyVisible()
        updateBtnClearVisible()
        updateBtnNextVisible()
    }

    private fun updateTxtEmptyVisible() {
        txtEmpty.isVisible = adapter.itemCount <= 0
    }

    private fun updateBtnNextVisible() {
        btnNext.isVisible = adapter.itemCount > 0
        txtNext.isVisible = adapter.itemCount > 0
    }

    private fun updateBtnClearVisible() {
        btnClearAll.isVisible = adapter.itemCount > 0
        txtClear.isVisible = adapter.itemCount > 0
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MediaListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MediaListFragment().apply {}
    }
}