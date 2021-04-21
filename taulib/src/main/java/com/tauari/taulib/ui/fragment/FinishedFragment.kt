package com.tauari.taulib.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.tauari.taulib.BundleKey
import com.tauari.taulib.R
import com.tauari.taulib.tool.FileTool

open class FinishedFragment : AnimatedFragment(), OnGotoNextFragment {
    protected var numItemsProcessed: Int = 0
    protected var outputDir: String = ""
    private lateinit var btnDone: Button
    private lateinit var txtTellResult: TextView
    private lateinit var txtTellOutputDir: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            numItemsProcessed = it.getInt(BundleKey.NUM_ITEMS_FINISHED)
            outputDir = it.getString(BundleKey.OUTPUT_DIR).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews()
        registerButtonClickListeners()
        updateViews()
    }

    protected fun findViews() {
        btnDone = requireActivity().findViewById(R.id.btn_done)
        txtTellResult = requireActivity().findViewById(R.id.txt_tell_result)
        txtTellOutputDir = requireActivity().findViewById(R.id.txt_tell_output_dir)
    }

    protected fun registerButtonClickListeners() {
        btnDone.setOnClickListener {
            gotoNextFragment()
        }
    }

    override fun gotoNextFragment() {
        //Need to implement from children
    }

    protected fun updateViews() {
        if(numItemsProcessed > 1) {
            txtTellResult.text = getString(R.string.tell_result_many_items_holder, numItemsProcessed)
        }
        else {
            txtTellResult.text = getString(R.string.tell_result_1_item_holder, numItemsProcessed)
        }

        txtTellOutputDir.text = FileTool.getDisplayPath(outputDir)
    }

    companion object {
        @JvmStatic
        fun newInstance(itemCount: Int, dir: String) =
            FinishedFragment().apply {
                arguments = Bundle().apply {
                    putInt(BundleKey.NUM_ITEMS_FINISHED, itemCount)
                    putString(BundleKey.OUTPUT_DIR, dir)
                }
            }
    }
}