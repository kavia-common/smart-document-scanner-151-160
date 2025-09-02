package com.camscanner.app.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.camscanner.app.R
import com.camscanner.app.storage.entities.Document

/**
 * PUBLIC_INTERFACE
 * DocumentsListAdapter
 * Classic RecyclerView adapter that inflates item_document.xml and binds fields without ViewBinding.
 */
class DocumentsListAdapter :
    ListAdapter<Document, DocumentsListAdapter.DocumentVH>(Diff) {

    private val selected = LinkedHashSet<Long>()

    // PUBLIC_INTERFACE
    fun getSelected(): List<Document> = currentList.filter { selected.contains(it.id) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentVH(view)
    }

    override fun onBindViewHolder(holder: DocumentVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DocumentVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        private val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        private val imgPreview: ImageView = itemView.findViewById(R.id.imgPreview)
        private val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bind(item: Document) {
            txtTitle.text = item.title
            txtDate.text = item.createdAt.toString()
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = selected.contains(item.id)

            val preview = item.previewPath
            if (preview != null) {
                val bmp = BitmapFactory.decodeFile(preview)
                imgPreview.setImageBitmap(bmp)
            } else {
                imgPreview.setImageDrawable(null)
            }

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) selected.add(item.id) else selected.remove(item.id)
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Document, newItem: Document) = oldItem == newItem
    }
}
