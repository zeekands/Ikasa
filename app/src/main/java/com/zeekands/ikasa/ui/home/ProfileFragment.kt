package com.zeekands.ikasa.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zeekands.ikasa.MainActivity
import com.zeekands.ikasa.databinding.FragmentProfileBinding
import com.zeekands.ikasa.db.LoginHelper
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var loginHelper: LoginHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginHelper = LoginHelper.getInstance(requireContext())
        loginHelper.open()
        binding.btnLogout.setOnClickListener {
logout()
        }
    }

    fun logout() {
        lifecycleScope.launch {
            loginHelper.delete()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }
}