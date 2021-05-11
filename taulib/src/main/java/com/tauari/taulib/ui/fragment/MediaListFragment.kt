package com.tauari.taulib.ui.fragment

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

    protected lateinit var btnNext: FloatingActionButton
    protected lateinit var btnAddPhotos: FloatingActionButton
    protected lateinit var btnClearAll: FloatingActionButton
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var adapter: MediaItemListAdapter
    protected lateinit var txtEmpty: TextView
    protected lateinit var txtNext: TextView
    protected lateinit var txtClear: TextView
    protected val observerImp = AdapterDataObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    open fun initAdapter() {
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
        findViews(view)
        initRecyclerView()
        registerButtonClickListeners()
        updateViewsVisible()
    }

    open fun findViews(container: View) {
        btnAddPhotos = container.findViewById(R.id.fbtn_add_photos)
        btnClearAll = container.findViewById(R.id.fbtn_clear_all)
        btnNext = container.findViewById(R.id.fbtn_next)
        recyclerView = container.findViewById(R.id.recy_medias)
        txtEmpty = container.findViewById(R.id.txt_empty_photos)
        txtNext = container.findViewById(R.id.txt_next)
        txtClear = container.findViewById(R.id.txt_clear)
    }



    open fun initRecyclerView() {
        recyclerView.addItemDecoration(ListItemDivider(requireContext()))
        val linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    open fun registerButtonClickListeners() {
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

    open fun getItemsReadyToProcess(): Bundle {
        val data = Bundle()
        data.putParcelableArrayList(BundleKey.MEDIAS_READY_TO_PROCESS, adapter.dataSource)
        return data
    }

    open fun clearAllItems() {
        adapter.clearDataSource()
    }

    open fun pickFiles() {
        Log.e("MediaListFragment", "mime type: ${getItemMimeType()}")
        val actionPickIntent = Intent(Intent.ACTION_PICK)
        actionPickIntent.type = getItemMimeType()
        actionPickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        actionPickIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(actionPickIntent, RequestCode.PICK_FILES_FROM_OS)
    }

    open fun getItemMimeType(): String {
        return "image/*"
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

    open fun updateViewsVisible() {
        updateTxtEmptyVisible()
        updateBtnClearVisible()
        updateBtnNextVisible()
    }

    open fun updateTxtEmptyVisible() {
        txtEmpty.isVisible = adapter.itemCount <= 0
    }

    open fun updateBtnNextVisible() {
        btnNext.isVisible = adapter.itemCount > 0
        txtNext.isVisible = adapter.itemCount > 0
    }

    open fun updateBtnClearVisible() {
        btnClearAll.isVisible = adapter.itemCount > 0
        txtClear.isVisible = adapter.itemCount > 0
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MediaListFragment().apply {}
    }
}