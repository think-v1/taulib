package com.tauari.taulib.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class MediaItem(val name: String, val path: String, val uriContent: Uri, val size: Long): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString()?: "",
            Uri.parse(parcel.readString()),
            parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeString(uriContent.toString())
        parcel.writeLong(size)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaItem> {
        override fun createFromParcel(parcel: Parcel): MediaItem {
            return MediaItem(parcel)
        }

        override fun newArray(size: Int): Array<MediaItem?> {
            return arrayOfNulls(size)
        }
    }
}