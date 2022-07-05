package com.zeekands.ikasa.ui.home

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var itemCartAdapter: ItemCartAdapter
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
    }

    private fun loadCartAsync() {
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val cartHelper = CartHelper.getInstance(requireContext())
            cartHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = cartHelper.queryAll()
                MappingHelper.mapCartCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val transaksis = deferredNotes.await()
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
        }
    }

}