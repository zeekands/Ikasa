package com.zeekands.ikasa.ui.register

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zeekands.ikasa.MainActivity
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.User
import com.zeekands.ikasa.databinding.ActivityRegisterBinding
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.UserHelper

class Register : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userHelper: UserHelper
    private var user: User? = null

    companion object {
        const val EXTRA_USER = "extra_user"
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_register) {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            if (nama.isEmpty()) {
                binding.etNama.error = "Field can not be blank"
                return
            }
            if (email.isEmpty()) {
                binding.etEmail.error = "Field can not be blank"
                return
            }
            if (password.isEmpty()) {
                binding.etPassword.error = "Field can not be blank"
                return
            }
            if (confirmPassword.isEmpty()) {
                binding.etConfirmPassword.error = "Field can not be blank"
                return
            }
            if (confirmPassword != password) {
                binding.etConfirmPassword.error = "Password and Confirm Password must be the same"
                return
            }

            user = User(0, nama, email, password, 0)
            var intent = Intent()
            intent.putExtra(EXTRA_USER, user)

            val values = ContentValues()
            values.put(DatabaseContract.UserColumns.NAMA, nama)
            values.put(DatabaseContract.UserColumns.EMAIL, email)
            values.put(DatabaseContract.UserColumns.PASSWORD, password)
            values.put(DatabaseContract.UserColumns.ROLE, 0)
            val result = userHelper.insert(values)
            if (result > 0) {
                user?.id = result.toInt()
                setResult(RESULT_ADD, intent)
                userHelper.close()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}