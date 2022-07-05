package com.zeekands.ikasa.ui.FormPesan

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.zeekands.ikasa.MainActivity
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.data.User
import com.zeekands.ikasa.databinding.ActivityFormPesanBinding
import com.zeekands.ikasa.db.CartHelper
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.TransaksiHelper
import com.zeekands.ikasa.ui.home.HomeActivity
import com.zeekands.ikasa.ui.register.Register

class FormPesanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormPesanBinding
    private lateinit var cartHelper: CartHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormPesanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<Ikan>("ikan")

        binding.tvTitle.text = data?.nama
        binding.tvHarga.text = data?.harga.toString()
        binding.tvDesc.text = data?.deskripsi

        binding.etJumlah.apply {
            setText("1")
            isEnabled = false
        }
        binding.btnMinus.setOnClickListener {
            if (binding.etJumlah.text.toString().toInt() > 1) {
                binding.etJumlah.setText((binding.etJumlah.text.toString().toInt() - 1).toString())
            }
        }
        binding.btnPlus.setOnClickListener {
            binding.etJumlah.setText((binding.etJumlah.text.toString().toInt() + 1).toString())
        }

        cartHelper = CartHelper.getInstance(applicationContext)
        cartHelper.open()

        binding.btnCart.setOnClickListener {
            val harga = data?.harga
            val id = data?.id
            val jumlah = binding.etJumlah.text.toString().toInt()
            val total = harga?.times(jumlah)

            Log.d("harga", harga.toString())
            Log.d("id", id.toString())
            Log.d("jumlah", jumlah.toString())
            Log.d("total", total.toString())

            val values = ContentValues()
            values.put(DatabaseContract.CartColumns.ID_USER, 1)
            values.put(DatabaseContract.CartColumns.ID_IKAN, 1)
            values.put(DatabaseContract.CartColumns.BERAT, jumlah)
            values.put(DatabaseContract.CartColumns.TOTAL, total)
            val result = cartHelper.insert(values)
            if (result > 0) {
                cartHelper.close()
                Toast.makeText(this, "Berhasil Masuk Ke Keranjang", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Gagal membuat transaksi", Toast.LENGTH_SHORT).show()
            }
        }

    }

}