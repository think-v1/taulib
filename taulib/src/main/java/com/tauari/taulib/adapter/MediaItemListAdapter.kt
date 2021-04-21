package com.tauari.taulib.adapter

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tauari.taulib.R
import com.tauari.taulib.data.model.MediaItem
import com.tauari.taulib.tool.FileTool

class MediaItemListAdapter(private val context: Context, val dataSource: ArrayList<MediaItem>): RecyclerView.Adapter<MediaItemViewHolder>(), OnMediaItemClickListener {
    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemViewHolder {
        val itemView = inflater.inflate(R.layout.item_media, parent, false)
        return MediaItemViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MediaItemViewHolder, position: Int) {
        holder.bind(getItemAt(position))
    }

    fun getItemAt(position: Int): MediaItem {
        return dataSource[position]
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onMediaItemClick(position: Int) {
//        TODO("Not yet implemented")
    }

    override fun onRemoveItemAt(position: Int) {
        dataSource.removeAt(position)
        (context as Activity).runOnUiThread {
            notifyItemRemoved(position)
        }
    }

    fun insertFromClipData(data: ClipData) {
        val startIndex = itemCount
        val count = data.itemCount
        for(i in 0 until count) {
            insertFromUriWithoutNotify(data.getItemAt(i).uri)
        }
        (context as Activity).runOnUiThread {
            notifyItemRangeInserted(startIndex, count)
        }
    }

    private fun insertFromUriWithoutNotify(uri: Uri) {
        uri.path?.let { path ->
            val info = FileTool.getImageInfo(context, uri)
            info?.let {
                val name = it.getString(OpenableColumns.DISPLAY_NAME, "UnNamed")
                val size = it.getLong(OpenableColumns.SIZE, 0L)
                dataSource.add(MediaItem(name, path, uri, size))
            }
        }
    }

    fun insertFromUri(uri: Uri) {
        insertFromUriWithoutNotify(uri)
        (context as Activity).runOnUiThread {
            notifyItemInserted(itemCount - 1)
        }
    }

    fun clearDataSource() {
        dataSource.clear()
        (context as Activity).runOnUiThread {
            notifyDataSetChanged()
        }
    }
}