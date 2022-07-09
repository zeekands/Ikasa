package com.zeekands.ikasa.ui.admin

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.ui.login.MainActivity
import com.zeekands.ikasa.utils.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.databinding.ActivityFormIkanBinding
import com.zeekands.ikasa.db.*
import com.zeekands.ikasa.ui.home.main.ItemIkanAdapter
import com.zeekands.ikasa.ui.home.pesanan.ItemPesananAdapter
import com.zeekands.ikasa.ui.register.Register
import com.zeekands.ikasa.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FormIkanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormIkanBinding
    private lateinit var itemIkanAdapter: ItemIkanAdapter
    private lateinit var itemPesananAdapter: ItemPesananAdapter
    private lateinit var ikanHelper: IkanHelper
    private lateinit var transaksiHelper: TransaksiHelper
    private lateinit var loginHelper: LoginHelper
    private var ikan: Ikan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ikanHelper = IkanHelper.getInstance(applicationContext)
        ikanHelper.open()
        transaksiHelper = TransaksiHelper.getInstance(applicationContext)
        transaksiHelper.open()
        loginHelper = LoginHelper.getInstance(applicationContext)
        loginHelper.open()

        binding.btnAdd.setOnClickListener(this)
        binding.btnSwitch.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnAddImage.setOnClickListener{
            openGalleryForImage()
        }
        loadIkanAsync()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_add) {
            if (title.isEmpty()) {
                binding.etTitle.error = "Field can not be blank"
                return
            }
            if (binding.etPrice.text?.isEmpty() == true) {
                binding.etPrice.error = "Field can not be blank"
                return
            }
            if (binding.etPrice.text.isNullOrBlank()) {
                binding.etStock.error = "Field can not be blank"
                return
            }
            if (binding.etDesc.text.toString().isEmpty()) {
                binding.etDesc.error = "Field can not be blank"
                return
            }
            val title = binding.etTitle.text.toString().trim()
            val price = binding.etPrice.text.toString().toInt()
            val stock = binding.etStock.text.toString().toInt()
            val desc = binding.etDesc.text.toString().trim()


            val bitmapDrawable : Bitmap = (binding.ivImageIkan.drawable as BitmapDrawable).bitmap
            val values = ContentValues()
            values.put(DatabaseContract.IkanColumns.NAMA, title)
            values.put(DatabaseContract.IkanColumns.DESKRIPSI, desc)
            values.put(DatabaseContract.IkanColumns.HARGA, price)
            values.put(DatabaseContract.IkanColumns.STOCK, stock)
            values.put(DatabaseContract.IkanColumns.GAMBAR, Utils.getBytes(bitmapDrawable))
            val result = ikanHelper.insert(values)
            if (result > 0) {
                ikan?.id = result.toInt()
                setResult(Register.RESULT_ADD, intent)
                Toast.makeText(this, "Berhasil menambah data ikan", Toast.LENGTH_SHORT).show()
                binding.etTitle.text?.clear()
                binding.etDesc.text?.clear()
                binding.etPrice.text?.clear()
                binding.etStock.text?.clear()
                binding.ivImageIkan.visibility = View.INVISIBLE
                binding.btnAddImage.visibility = View.VISIBLE
                loadIkanAsync()
//                intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal membuat data ikan", Toast.LENGTH_SHORT).show()
            }
        }
        else if (view.id == R.id.btn_switch) {
            when (binding.btnSwitch.text){
                "List Pesanan" -> loadPesananAsync()
                "List Ikan" -> loadIkanAsync()
            }
        }
        else if (view.id == R.id.btn_logout) {
           logout()
        }
    }

    private fun loadPesananAsync() {
        binding.btnSwitch.text = "List Ikan"
        binding.tvListIkan.text = "List Pesanan"
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = transaksiHelper.queryAll()
                MappingHelper.mapTransaksiCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val transaksis = deferredNotes.await()
            if (transaksis.size > 0) {
                binding.tvNoData.visibility = View.INVISIBLE
                itemPesananAdapter = ItemPesananAdapter()
                itemPesananAdapter.listTransaksi = transaksis
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemPesananAdapter
                }
            } else {
                itemPesananAdapter = ItemPesananAdapter()
                itemPesananAdapter.listTransaksi = transaksis
                binding.rv.adapter = itemPesananAdapter
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun loadIkanAsync() {
        binding.btnSwitch.text = "List Pesanan"
        binding.tvListIkan.text = "List Ikan"
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = ikanHelper.queryAll()
                MappingHelper.mapIkanCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val ikans = deferredNotes.await()
            if (ikans.size > 0) {
                binding.tvNoData.visibility = View.INVISIBLE
                itemIkanAdapter = ItemIkanAdapter()
                itemIkanAdapter.ListIkan = ikans
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemIkanAdapter
                }
            } else {
                itemIkanAdapter = ItemIkanAdapter()
                itemIkanAdapter.ListIkan = ikans
                binding.rv.adapter = itemIkanAdapter
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    fun logout(){
        loginHelper.open()
        loginHelper.delete()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        startActivity(intent)
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
            binding.btnAddImage.visibility = View.INVISIBLE
        }
    }
}