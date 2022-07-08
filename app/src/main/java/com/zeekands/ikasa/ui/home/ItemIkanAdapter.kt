package com.zeekands.ikasa.ui.home

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.databinding.ItemCartBinding
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.ui.EditIkanActivity
import com.zeekands.ikasa.utils.Utils
import java.io.File


class ItemIkanAdapter : RecyclerView.Adapter<ItemIkanAdapter.ItemCartViewHolder>() {
    private lateinit var ikanHelper: IkanHelper

    var ListIkan = ArrayList<Ikan>()
        set(ListIkan) {
            if (ListIkan.size > 0) {
                this.ListIkan.clear()
            }
            this.ListIkan.addAll(ListIkan)
        }

    fun addItem(ikan: Ikan) {
        this.ListIkan.add(ikan)
        notifyItemInserted(this.ListIkan.size - 1)
    }

    fun updateItem(position: Int, ikan: Ikan) {
        this.ListIkan[position] = ikan
        notifyItemChanged(position, ikan)
    }

    fun removeItem(position: Int) {
        this.ListIkan.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.ListIkan.size)
    }

    inner class ItemCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCartBinding.bind(itemView)
        fun bind(ikan: Ikan) {
            binding.tvNama.text = ikan.nama
            binding.tvHarga.text = "Rp. ${ikan.harga.toString()} /kg"
            binding.tvStatus.text = "Stock ${ikan.stock}"
            binding.ivItem.setImageBitmap(Utils.getImage(ikan.gambar))

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, EditIkanActivity::class.java)
                intent.putExtra(EditIkanActivity.ID, ikan.id)
                intent.putExtra(EditIkanActivity.NAMA, ikan.nama)
                intent.putExtra(EditIkanActivity.DESC, ikan.deskripsi)
                intent.putExtra(EditIkanActivity.HARGA, ikan.harga)
                intent.putExtra(EditIkanActivity.STOK, ikan.stock)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ItemCartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemCartViewHolder, position: Int) {
        holder.bind(ListIkan[position])
    }

    override fun getItemCount(): Int = this.ListIkan.size

}