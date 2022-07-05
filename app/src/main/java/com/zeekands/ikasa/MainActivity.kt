package com.zeekands.ikasa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zeekands.ikasa.databinding.ActivityMainBinding
import com.zeekands.ikasa.ui.home.HomeActivity
import com.zeekands.ikasa.ui.register.Register

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide();
        setContentView(binding.root)

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}