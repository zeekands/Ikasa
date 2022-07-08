package com.zeekands.ikasa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.FragmentProfileBinding
import com.zeekands.ikasa.db.LoginHelper
import com.zeekands.ikasa.db.UserHelper

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var loginHelper: LoginHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginHelper = LoginHelper.getInstance(requireContext())

        binding.btnLogout.setOnClickListener {

        }
    }

    fun logout(){
        loginHelper.open()
        loginHelper.delete()
    }
}