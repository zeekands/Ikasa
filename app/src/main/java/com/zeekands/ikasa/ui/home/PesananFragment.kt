package com.zeekands.ikasa.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.FragmentHomeBinding
import com.zeekands.ikasa.databinding.FragmentPesananBinding
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.TransaksiHelper
import com.zeekands.ikasa.db.UserHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PesananFragment : Fragment() {
    private lateinit var binding: FragmentPesananBinding
    private lateinit var itemPesananAdapter: ItemPesananAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPesananBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        loadNotesAsync()
        Log.d("PesananFragment", "onResume")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        loadNotesAsync()
        Log.d("onViewStateRestored", "onViewStateRestored")
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val transaksiHelper = TransaksiHelper.getInstance(requireContext())
            transaksiHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = transaksiHelper.queryAll()
                MappingHelper.mapTransaksiCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val transaksis = deferredNotes.await()
            if (transaksis.size > 0) {
                itemPesananAdapter = ItemPesananAdapter()
                itemPesananAdapter.listTransaksi = transaksis
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemPesananAdapter
                }
            } else {
                itemPesananAdapter = ItemPesananAdapter()
                itemPesananAdapter.listTransaksi = ArrayList()
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemPesananAdapter
                }
                Toast.makeText(requireContext(), "Tidak ada data", Toast.LENGTH_SHORT).show()
            }
            transaksiHelper.close()
        }
    }

}