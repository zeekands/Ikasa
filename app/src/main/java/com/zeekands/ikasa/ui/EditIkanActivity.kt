package com.zeekands.ikasa.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.ActivityEditIkanBinding
import com.zeekands.ikasa.databinding.ActivityFormIkanBinding
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.ui.register.Register

class EditIkanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditIkanBinding
    private lateinit var ikanHelper: IkanHelper

    companion object {
        var ID = "id"
        var NAMA = "nama"
        var DESC = "desc"
        var HARGA = "harga"
        var STOK = "stok"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etTitle.setText(intent.getStringExtra(NAMA).toString())
        binding.etDesc.setText(intent.getStringExtra(DESC).toString())
        binding.etPrice.setText(intent.getIntExtra(HARGA, 0).toString())
        binding.etStock.setText(intent.getIntExtra(STOK, 0).toString())

        ikanHelper = IkanHelper.getInstance(applicationContext)
        ikanHelper.open()

        binding.btnAdd.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val price = binding.etPrice.text.toString().toInt()
            val stock = binding.etStock.text.toString().toInt()
            val desc = binding.etDesc.text.toString().trim()
            if (title.isEmpty()) {
                binding.etTitle.error = "Field can not be blank"
            }
            if (price == null) {
                binding.etPrice.error = "Field can not be blank"
            }
            if (stock == null) {
                binding.etStock.error = "Field can not be blank"
            }
            if (desc.isEmpty()) {
                binding.etDesc.error = "Field can not be blank"
            }

            val values = ContentValues()
            values.put(DatabaseContract.IkanColumns.NAMA, title)
            values.put(DatabaseContract.IkanColumns.DESKRIPSI, desc)
            values.put(DatabaseContract.IkanColumns.HARGA, price)
            values.put(DatabaseContract.IkanColumns.STOCK, stock)
            val result = ikanHelper.update(intent.getIntExtra(ID, 0).toString(), values)
            if (result > 0) {
                Toast.makeText(this, "Berhasil mengubah data ikan", Toast.LENGTH_SHORT).show()
                intent = Intent(this, FormIkanActivity::class.java)
                startActivity(intent)
            } else {
                Log.d("result", result.toString())
                Toast.makeText(this, "Gagal mengubah data ikan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}