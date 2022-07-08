package com.zeekands.ikasa

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.zeekands.ikasa.data.Login
import com.zeekands.ikasa.data.User
import com.zeekands.ikasa.databinding.ActivityMainBinding
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.LoginHelper
import com.zeekands.ikasa.db.UserHelper
import com.zeekands.ikasa.ui.FormPesan.FormPesanActivity
import com.zeekands.ikasa.ui.home.HomeActivity
import com.zeekands.ikasa.ui.register.Register
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginHelper: LoginHelper

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val RESULT_ADD = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide();

        lifecycleScope.launch {
            loginHelper = LoginHelper.getInstance(applicationContext)
            loginHelper.open()
            val deferredUser = async(Dispatchers.IO) {
                val cursor = loginHelper.queryAll()
                MappingHelper.mapLoginCursorToArrayList(cursor)
            }

            val user = deferredUser.await()

            if (user.size == 1) {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                setContentView(binding.root)
            }
        }



        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        binding.btnLogin.setOnClickListener {
            Log.d("dataLogin", binding.etEmail.text.toString()+password)
            getDataLogin(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun getDataLogin(email : String, password : String){
        lifecycleScope.launch {
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.login(email, password)
                MappingHelper.mapUserCursorToArrayList(cursor)
            }

            val user = deferredUser.await()

            if(user.size == 1 ){
                val login = Login(user[0].id,user[0].nama)
                var intent = Intent()
                intent.putExtra(EXTRA_LOGIN, login)

                val values = ContentValues()
                values.put(DatabaseContract.LoginColumns.ID_USER, user[0].id)
                values.put(DatabaseContract.LoginColumns.NAMA_USER, user[0].nama)
                val result = loginHelper.insert(values)
                loginHelper.close()

                intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(applicationContext, "Tidak ada data user", Toast.LENGTH_SHORT).show()
            }
            userHelper.close()
        }
    }
}