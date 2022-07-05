package com.zeekands.ikasa.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.databinding.ItemCardBinding
import com.zeekands.ikasa.ui.FormPesan.FormPesanActivity

class ItemIkanPopulerAdapter: RecyclerView.Adapter<ItemIkanPopulerAdapter.ItemIkanPopulerViewHolder>() {
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
    inner class ItemIkanPopulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCardBinding.bind(itemView)
        fun bind(ikan: Ikan) {
            binding.tvTitle.text = ikan.nama
            binding.tvHarga.text = "Rp. ${ikan.harga}/kg"
            binding.imageButton2.setOnClickListener {
                val intent = Intent(itemView.context, FormPesanActivity::class.java)
                intent.putExtra("ikan", ikan)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemIkanPopulerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemIkanPopulerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemIkanPopulerViewHolder, position: Int) {
        holder.bind(listIkan[position])
    }

    override fun getItemCount(): Int = this.listIkan.size
}