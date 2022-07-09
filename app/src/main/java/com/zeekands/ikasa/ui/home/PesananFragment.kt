package com.zeekands.ikasa.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.zeekands.ikasa.databinding.FragmentPesananBinding

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
//        loadNotesAsync()
        Log.d("PesananFragment", "onResume")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        loadNotesAsync()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        val tabAdapter = TabAdapter(requireActivity(), childFragmentManager)
        binding.viewpager.adapter = tabAdapter
        binding.viewpagertab.setViewPager(binding.viewpager)
        binding.viewpager.currentItem = 0
    }
}