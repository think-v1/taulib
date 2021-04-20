package com.tauari.taulib.task

interface OnProcessingTaskListener {
    fun updateProgress(progress: Int, message: String)
}