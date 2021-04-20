package com.tauari.taulib.tool

class FileSizeCalculator {
    fun getFileSizeWithUnit(sizeInLong: Long): Pair<Double, String> {
        val size = sizeInLong.toDouble()
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