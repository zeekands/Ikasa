package com.zeekands.ikasa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zeekands.ikasa.databinding.ActivityMainBinding
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.UserHelper
import com.zeekands.ikasa.ui.FormIkanActivity
import com.zeekands.ikasa.ui.home.HomeActivity
import com.zeekands.ikasa.ui.register.Register

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide();
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(this)
        userHelper.open()

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val cursor = userHelper.queryByEmail(binding.etEmail.text.toString().trim())
        MappingHelper.mapUserCursorToUser(cursor).also {
            if (it != null) {
                if (binding.etPassword.text.toString().trim() == it.password) {
                    when (it.role){
                        0 -> {
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        1 -> {
                            startActivity(Intent(this, FormIkanActivity::class.java))
                        }
                    }
                } else {
                    Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Anda belum terdaftar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}