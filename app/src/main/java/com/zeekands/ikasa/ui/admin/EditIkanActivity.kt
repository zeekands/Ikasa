package com.zeekands.ikasa.ui.admin

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zeekands.ikasa.utils.MappingHelper
import com.zeekands.ikasa.databinding.ActivityEditIkanBinding
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.utils.Utils
import kotlinx.coroutines.launch

class EditIkanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditIkanBinding
    private lateinit var ikanHelper: IkanHelper

    companion object {
        var ID = "id"
        var NAMA = "nama"
        var DESC = "desc"
        var HARGA = "harga"
        var STOK = "stok"
        var GAMBAR = "gambar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etTitle.setText(intent.getStringExtra(NAMA).toString())
        binding.etDesc.setText(intent.getStringExtra(DESC).toString())
        binding.etPrice.setText(intent.getIntExtra(HARGA, 0).toString())
        binding.etStock.setText(intent.getIntExtra(STOK, 0).toString())
        getImage()
        binding.btnAddImage.setOnClickListener{
            openGalleryForImage()
        }

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
            val bitmapDrawable : Bitmap = (binding.ivImageIkan.drawable as BitmapDrawable).bitmap
            val bitmap = Utils.getBytes(bitmapDrawable)

            val values = ContentValues()
            values.put(DatabaseContract.IkanColumns.NAMA, title)
            values.put(DatabaseContract.IkanColumns.DESKRIPSI, desc)
            values.put(DatabaseContract.IkanColumns.HARGA, price)
            values.put(DatabaseContract.IkanColumns.STOCK, stock)
            values.put(DatabaseContract.IkanColumns.GAMBAR, bitmap)
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

    fun getImage(){
        lifecycleScope.launch {
            ikanHelper = IkanHelper.getInstance(applicationContext)
            ikanHelper.open()
            val cursor = ikanHelper.queryById(intent.getIntExtra(ID, 0).toString())
            MappingHelper.mapIkanCursorToIkan(cursor)?.also {
                binding.ivImageIkan.setImageBitmap(Utils.getImage(it.gambar))
            }
        }
    }

    val REQUEST_CODE = 100
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            binding.ivImageIkan.setImageURI(data?.data)
        }
    }
}