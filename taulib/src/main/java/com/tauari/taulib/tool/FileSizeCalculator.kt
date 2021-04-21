package com.tauari.taulib.tool

object FileSizeCalculator {
    fun getFileSizeDisplay(sizeInByte: Long, format: String = "%.2f"): String {
        val (size, unitText) = getFileSizeWithUnit(sizeInByte)
        return "$format $unitText".format(size)
    }

    fun getFileSizeWithUnit(sizeInByte: Long): Pair<Double, String> {
        val size = sizeInByte.toDouble()
        val numBytePerKb = 1024.0
        return when {
            size < numBytePerKb -> {
                Pair(size, "B")
            }
            size / numBytePerKb < numBytePerKb -> {
                Pair((size / numBytePerKb), "Kb")
            }
            else -> {
                Pair((size / numBytePerKb / numBytePerKb), "Mb")
            }
        }

    }
}