package com.tauari.taulib.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tauari.taulib.R
import com.tauari.taulib.data.model.MediaItem
import com.tauari.taulib.tool.FileSizeCalculator

class MediaItemViewHolder(itemView: View, val listener: OnMediaItemClickListener): RecyclerView.ViewHolder(itemView) {
    private val imgAvatar = itemView.findViewById<ImageView>(R.id.img_avatar)
    private val txtName = itemView.findViewById<TextView>(R.id.txt_photo_name)
    private val txtPath = itemView.findViewById<TextView>(R.id.txt_photo_path)
    private val ibtnDelete = itemView.findViewById<ImageButton>(R.id.ibtn_delete_item)
    init {
        ibtnDelete.setOnClickListener {
            listener.onRemoveItemAt(adapterPosition)
        }
    }
    fun bind(item: MediaItem) {
        setNameText(item.name)
        setSizeText(FileSizeCalculator.getFileSizeDisplay(item.size))
        setThumbnail(itemView.context, item.uriContent)
    }

    fun setNameText(value: String) {
        txtName.text = value
    }

    fun setSizeText(value: String) {
        txtPath.text = value
    }

    fun setThumbnail(context: Context, uriContent: Uri) {
        Glide.with(context).load(uriContent).into(imgAvatar)
    }
}