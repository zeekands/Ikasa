package com.zeekands.ikasa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Cart
import com.zeekands.ikasa.databinding.ItemCheckoutBinding
import com.zeekands.ikasa.db.IkanHelper

class ItemCartAdapter: RecyclerView.Adapter<ItemCartAdapter.ItemCartViewHolder>() {
    private lateinit var ikanHelper: IkanHelper
    var ListCart = ArrayList<Cart>()
        set(ListCart) {
            if (ListCart.size > 0) {
                this.ListCart.clear()
            }
            this.ListCart.addAll(ListCart)
        }
    fun addItem(cart: Cart) {
        this.ListCart.add(cart)
        notifyItemInserted(this.ListCart.size - 1)
    }
    fun updateItem(position: Int, cart: Cart) {
        this.ListCart[position] = cart
        notifyItemChanged(position, cart)
    }
    fun removeItem(position: Int) {
        this.ListCart.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.ListCart.size)
    }

    inner class ItemCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCheckoutBinding.bind(itemView)
        fun bind(cart: Cart) {
            ikanHelper = IkanHelper.getInstance(itemView.context)
            ikanHelper.open()
            val cursor = ikanHelper.queryById(cart.id.toString())
            MappingHelper.mapIkanCursorToIkan(cursor).also {
                binding.tvTitle.text = it.nama
            }
            binding.tvHarga.text = "Rp.${cart.total.toString()} (${cart.total} Kg)"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_checkout, parent, false)
        return ItemCartViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemCartViewHolder, position: Int) {
        holder.bind(ListCart[position])
    }

    override fun getItemCount(): Int = this.ListCart.size
}