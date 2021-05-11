package com.tauari.taulib.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.tauari.taulib.BundleKey
import com.tauari.taulib.R
import com.tauari.taulib.RequestCode
import com.tauari.taulib.data.model.MediaItem
import com.tauari.taulib.task.OnProcessingTaskListener
import com.tauari.taulib.tool.FileTool
import com.tauari.taulib.tool.PrefTool
import com.tauari.taulib.tool.TreeDirectoryUriPathHelper
import java.io.File

abstract class ReadyToProcessFragment: AnimatedFragment(), OnGotoNextFragment, OnProcessingTaskListener, Loader.OnLoadCanceledListener<Boolean>, LoaderManager.LoaderCallbacks<Boolean> {
    protected lateinit var pref: SharedPreferences
    protected lateinit var txtOutputDir: TextView
    protected lateinit var btnChangeOutputDir: Button
    protected lateinit var proressBar: ProgressBar
    protected lateinit var txtProgressMessage: TextView
    protected lateinit var taskLoaderManager: LoaderManager
    protected lateinit var btnExecute: Button

    protected var dataSource: ArrayList<MediaItem> = arrayListOf()
    protected var defaultOutputDir: File? = null
    protected var selectedOutputDir: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataSource = getDataIncome()
        pref = getPrefKey()
        taskLoaderManager = getAppTaskLoaderManager()
    }

    open fun getDataIncome(): ArrayList<MediaItem> {
        return if(arguments != null) {
            arguments!!.getParcelableArrayList(BundleKey.MEDIAS_READY_TO_PROCESS) ?: arrayListOf()
        } else {
            arrayListOf()
        }
    }

    abstract fun getPrefKey(): SharedPreferences

    abstract fun getAppTaskLoaderManager(): LoaderManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        registerViewListeners()
        setNormalState()
        initDefaultValues()
    }

    abstract fun findViews(container: View)
    open fun registerViewListeners() {
        registerExecuteButtonListener()
        registerChangeDirButtonListener()
    }
    open fun registerExecuteButtonListener() {
        btnExecute.setOnClickListener { execute() }
    }
    open fun registerChangeDirButtonListener() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            btnChangeOutputDir.setOnClickListener { openDirPicker() }
        }
    }
    open fun setNormalState() {
        btnExecute.isEnabled = true
        btnExecute.text = getString(R.string.start)
        btnExecute.isClickable = true
        btnExecute.alpha = 1.0F

        btnChangeOutputDir.isVisible = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

        proressBar.isVisible = false
        txtProgressMessage.isVisible = false
    }
    open fun initDefaultValues() {
        defaultOutputDir = getAppDefaultOutputDir()
        val prevOutputPath = pref.getString(getOutputDirPrefKey(), defaultOutputDir?.path)
        selectedOutputDir = if(prevOutputPath!= null) {
            File(prevOutputPath)
        } else {
            defaultOutputDir
        }
        txtOutputDir.text = FileTool.getDisplayPath(selectedOutputDir)
    }

    abstract fun getAppDefaultOutputDir(): File?

    abstract fun getOutputDirPrefKey(): String

    open fun execute() {
        setProcessingState()
        val data = getDataForProcessing()
        val loader = getTaskLoader(data)
        loader.forceLoad()
    }

    open fun getDataForProcessing(): Bundle {
        val data = Bundle()
        data.putParcelableArrayList(BundleKey.MEDIAS_READY_TO_PROCESS, dataSource)
        return data
    }

    open fun setProcessingState() {
        btnExecute.isEnabled = false
        btnExecute.text = getString(R.string.processing)
        btnExecute.isClickable = false
        btnExecute.alpha = 0.5F

        btnChangeOutputDir.isVisible = false

        proressBar.isVisible = true
        txtProgressMessage.isVisible = true
        txtProgressMessage.text = getString(R.string.progress_message_holder, 0, dataSource.size)
    }

    abstract fun getTaskLoader(data: Bundle): Loader<Boolean?>

    open fun openDirPicker() {
        val openDirIntent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(openDirIntent, RequestCode.PICK_DIR_FROM_OS)
    }

    override fun updateProgress(progress: Int, message: String) {
        requireActivity().runOnUiThread {
            txtProgressMessage.text = message
        }
    }

    override fun onLoadCanceled(loader: Loader<Boolean>) {
        if(loader.id == getTaskId()) {
            taskLoaderManager.destroyLoader(loader.id)
        }
    }

    open fun cancel() {
//        if(taskLoaderManager.hasRunningLoaders()) {
//            Toast.makeText(requireContext(), getString(R.string.tell_processing_canceled), Toast.LENGTH_LONG).show()
//        }

        val loader = taskLoaderManager.getLoader<Loader<Boolean>>(getTaskId())
        loader?.let {
            Toast.makeText(requireContext(), getString(R.string.tell_processing_canceled), Toast.LENGTH_LONG).show()
            it.cancelLoad()
        }
    }

    abstract fun getTaskId(): Int

    override fun onLoadFinished(loader: Loader<Boolean>, data: Boolean?) {
        if(loader.id == getTaskId()) {
            taskLoaderManager.destroyLoader(loader.id)
            FileTool.scanOutputDir(requireContext(), defaultOutputDir?.path)
            setNormalState()
            gotoNextFragment()
        }
    }

    override fun onLoaderReset(loader: Loader<Boolean>) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RequestCode.PICK_DIR_FROM_OS && resultCode == Activity.RESULT_OK) {
            data?.let { dataIntent ->
                val uri = dataIntent.data
                uri?.let { outputUri ->
                    getDirFromPicker(outputUri)

                }
            }
        }
    }

    open fun getDirFromPicker(uri: Uri) {
        val path = TreeDirectoryUriPathHelper.getPathFromUri(requireContext(), uri)
        path?.let {
            txtOutputDir.text = FileTool.getDisplayPath(it)
            selectedOutputDir = File(it)
            PrefTool.applyStringToPref(pref, getOutputDirPrefKey(), it)
        }
    }

    open fun getResultData(): Bundle {
        val data = Bundle()
        data.putInt(BundleKey.NUM_ITEMS_FINISHED, dataSource.size)
        data.putString(BundleKey.OUTPUT_DIR, selectedOutputDir?.path)
        return data
    }



}