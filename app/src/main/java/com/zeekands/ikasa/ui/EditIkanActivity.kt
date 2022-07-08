package com.zeekands.ikasa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.ActivityEditIkanBinding
import com.zeekands.ikasa.databinding.ActivityFormIkanBinding

class EditIkanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditIkanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}