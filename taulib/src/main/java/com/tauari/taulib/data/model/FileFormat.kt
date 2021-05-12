package com.tauari.taulib.data.model

import android.os.Parcel
import android.os.Parcelable

data class FileFormat(val name: String, var isChecked: Boolean = false): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "Undefined",
        parcel.readInt() == 1) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(if(isChecked) { 1 } else { 0 })
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileFormat> {
        override fun createFromParcel(parcel: Parcel): FileFormat {
            return FileFormat(parcel)
        }

        override fun newArray(size: Int): Array<FileFormat?> {
            return arrayOfNulls(size)
        }
    }
}
