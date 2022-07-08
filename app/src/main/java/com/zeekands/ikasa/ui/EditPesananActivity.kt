package com.zeekands.ikasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.ActivityEditIkanBinding
import com.zeekands.ikasa.databinding.ActivityEditPesananBinding

class EditPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}