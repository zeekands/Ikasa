package com.zeekands.ikasa.ui.home

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.FragmentCartBinding
import com.zeekands.ikasa.db.CartHelper
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.LoginHelper
import com.zeekands.ikasa.db.TransaksiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var itemCartAdapter: ItemCartAdapter
    private lateinit var transaksiHelper: TransaksiHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCartAsync()
        binding.btnCheckout.setOnClickListener {
            checkOutAsync()
        }
    }

    private fun loadCartAsync() {
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val cartHelper = CartHelper.getInstance(requireContext())
            cartHelper.open()
            val loginHelper = LoginHelper.getInstance(requireContext())
            loginHelper.open()
            val idLogin = async (Dispatchers.IO){
                val cursor = loginHelper.queryAll()
                MappingHelper.mapLoginCursorToArrayList(cursor).first().id_user
            }

            val deferreCart = async(Dispatchers.IO) {
                val cursor = cartHelper.queryById(idLogin.await().toString())
                MappingHelper.mapCartCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val transaksis = deferreCart.await()
            if (transaksis.size > 0) {
                itemCartAdapter = ItemCartAdapter()
                itemCartAdapter.ListCart = transaksis
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemCartAdapter
                }
            } else {
                Toast.makeText(requireContext(), "Tidak ada data", Toast.LENGTH_SHORT).show()
            }
            cartHelper.close()
            loginHelper.close()
        }
    }

    private fun checkOutAsync() {
        lifecycleScope.launch {
            val cartHelper = CartHelper.getInstance(requireContext())
            cartHelper.open()
            val loginHelper = LoginHelper.getInstance(requireContext())
            loginHelper.open()
            val transaksiHelper = TransaksiHelper.getInstance(requireContext())
            transaksiHelper.open()
            val idLogin = async (Dispatchers.IO){
                val cursor = loginHelper.queryAll()
                MappingHelper.mapLoginCursorToArrayList(cursor).first().id_user
            }

            val deferredCart = async(Dispatchers.IO) {
                val cursor = cartHelper.queryById(idLogin.await().toString())
                MappingHelper.mapCartCursorToArrayList(cursor)
            }
            val transaksis = deferredCart.await()
            if (transaksis.size > 0) {
                for (transaksi in transaksis) {
                    val idTransaksi = async(Dispatchers.IO) {
                        val values = ContentValues()
                        values.put(DatabaseContract.TransaksiColumns.ID_USER, transaksi.id_user)
                        values.put(DatabaseContract.TransaksiColumns.ID_IKAN, transaksi.id_ikan)
                        values.put(DatabaseContract.TransaksiColumns.BERAT, transaksi.jumlah)
                        values.put(DatabaseContract.TransaksiColumns.TOTAL, transaksi.total)
                        values.put(DatabaseContract.TransaksiColumns.STATUS, "Proses")
                        val id = transaksiHelper.insert(
                            values
                        )
                        id
                    }
                    idTransaksi.await()
                }
                Toast.makeText(requireContext(), "Checkout berhasil", Toast.LENGTH_SHORT).show()
            }
            cartHelper.delete(idLogin.await().toString())
            itemCartAdapter.removeAll()
            cartHelper.close()
            loginHelper.close()
            transaksiHelper.close()
        }
    }
}