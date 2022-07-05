package com.zeekands.ikasa.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.databinding.FragmentHomeBinding
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.UserHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var itemAdapter: ItemIkanPopulerAdapter
    private lateinit var itemRoundedAdapter: ItemRoundedAdapter
    private lateinit var userHelper: UserHelper
    private val homeViewModel : HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")
        userHelper = UserHelper.getInstance(requireContext())
        userHelper.open()

        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        lifecycleScope.launch {
            print("loadNotesAsync")
            binding.progressBar.visibility = View.VISIBLE
            val ikanHelper = IkanHelper.getInstance(requireContext())
            ikanHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = ikanHelper.queryAll()
                MappingHelper.mapIkanCursorToArrayList(cursor)
            }
            val ikans = deferredNotes.await()
            if (ikans.size > 0) {
                itemAdapter = ItemIkanPopulerAdapter()
                itemRoundedAdapter = ItemRoundedAdapter()
                itemAdapter.listIkan = ikans
                itemRoundedAdapter.listIkan = ikans
                binding.gridView.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = itemAdapter
                }
                binding.linearView.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = itemRoundedAdapter
                }

            } else {
                Toast.makeText(requireContext(), "Tidak ada data", Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = View.GONE
            ikanHelper.close()
        }
    }
}