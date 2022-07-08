package com.zeekands.ikasa.ui.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.data.Transaksi
import com.zeekands.ikasa.databinding.ItemCheckoutBinding
import com.zeekands.ikasa.databinding.ItemPesananBinding
import com.zeekands.ikasa.databinding.ItemRoundedBinding
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.UserHelper
import com.zeekands.ikasa.ui.EditPesananActivity

class ItemPesananAdapter: RecyclerView.Adapter<ItemPesananAdapter.ItemPesananViewHolder>() {
    private lateinit var ikanHelper: IkanHelper
    var listTransaksi = ArrayList<Transaksi>()
        set(listTransaksi) {
            if (listTransaksi.size > 0) {
                this.listTransaksi.clear()
            }
            this.listTransaksi.addAll(listTransaksi)
        }
    fun addItem(transaksi: Transaksi) {
        this.listTransaksi.add(transaksi)
        notifyItemInserted(this.listTransaksi.size - 1)
    }
    fun updateItem(position: Int, transaksi: Transaksi) {
        this.listTransaksi[position] = transaksi
        notifyItemChanged(position, transaksi)
    }
    fun removeItem(position: Int) {
        this.listTransaksi.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listTransaksi.size)
    }

    inner class ItemPesananViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemPesananBinding.bind(itemView)
        fun bind(transaksi: Transaksi) {
            ikanHelper = IkanHelper.getInstance(itemView.context)
            ikanHelper.open()
            val cursor = ikanHelper.queryById(transaksi.idIkan.toString())
            MappingHelper.mapIkanCursorToIkan(cursor)?.also {
                binding.tvTitle.text = it.nama
            }
            binding.tvHarga.text = "Rp.${transaksi.total} (${transaksi.berat} Kg)"
            binding.tvStatus.text = transaksi.status

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, EditPesananActivity::class.java)
                intent.putExtra(EditPesananActivity.ID, transaksi.id)
                intent.putExtra(EditPesananActivity.ID_IKAN, transaksi.idIkan)
                intent.putExtra(EditPesananActivity.ID_USER, transaksi.idUser)
                intent.putExtra(EditPesananActivity.BERAT, transaksi.berat)
                intent.putExtra(EditPesananActivity.TOTAL, transaksi.total)
                intent.putExtra(EditPesananActivity.STATUS, transaksi.status)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPesananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan, parent, false)
        return ItemPesananViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPesananViewHolder, position: Int) {
        holder.bind(listTransaksi[position])
    }

    override fun getItemCount(): Int = this.listTransaksi.size
}