package com.zeekands.ikasa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.databinding.ItemRoundedBinding

class ItemRoundedAdapter: RecyclerView.Adapter<ItemRoundedAdapter.ItemRoundedViewHolder>() {
    var listIkan = ArrayList<Ikan>()
        set(listIkan) {
            if (listIkan.size > 0) {
                this.listIkan.clear()
            }
            this.listIkan.addAll(listIkan)
        }
    fun addItem(ikan: Ikan) {
        this.listIkan.add(ikan)
        notifyItemInserted(this.listIkan.size - 1)
    }
    fun updateItem(position: Int, ikan: Ikan) {
        this.listIkan[position] = ikan
        notifyItemChanged(position, ikan)
    }
    fun removeItem(position: Int) {
        this.listIkan.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listIkan.size)
    }
    inner class ItemRoundedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRoundedBinding.bind(itemView)
        fun bind(ikan: Ikan) {
            binding.textView.text = ikan.nama
            binding.imageView.setBackgroundColor(itemView.context.getColor(R.color.main_blue_dark))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRoundedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rounded, parent, false)
        return ItemRoundedViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemRoundedViewHolder, position: Int) {
        holder.bind(listIkan[position])
    }

    override fun getItemCount(): Int = this.listIkan.size
}