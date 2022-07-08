package com.zeekands.ikasa.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.MainActivity
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.UpdateIkanActivity
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.data.User
import com.zeekands.ikasa.databinding.ActivityFormIkanBinding
import com.zeekands.ikasa.databinding.ActivityRegisterBinding
import com.zeekands.ikasa.db.CartHelper
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.UserHelper
import com.zeekands.ikasa.ui.home.ItemCartAdapter
import com.zeekands.ikasa.ui.home.ItemIkanAdapter
import com.zeekands.ikasa.ui.register.Register
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FormIkanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormIkanBinding
    private lateinit var itemIkanAdapter: ItemIkanAdapter
    private lateinit var ikanHelper: IkanHelper
    private var ikan: Ikan? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormIkanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ikanHelper = IkanHelper.getInstance(applicationContext)
        ikanHelper.open()

        binding.btnAdd.setOnClickListener(this)
        loadCartAsync()
    }

    private fun loadCartAsync() {
        lifecycleScope.launch {
//            binding.progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = ikanHelper.queryAll()
                MappingHelper.mapIkanCursorToArrayList(cursor)
            }
//            binding.progressbar.visibility = View.INVISIBLE
            val ikans = deferredNotes.await()
            if (ikans.size > 0) {
                itemIkanAdapter = ItemIkanAdapter()
                itemIkanAdapter.ListIkan = ikans
                binding.rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = itemIkanAdapter
                }
            } else {
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_add) {
            val title = binding.etTitle.text.toString().trim()
            val price = binding.etPrice.text.toString().toInt()
            val stock = binding.etStock.text.toString().toInt()
            val desc = binding.etDesc.text.toString().trim()
            if (title.isEmpty()) {
                binding.etTitle.error = "Field can not be blank"
                return
            }
            if (price == null) {
                binding.etPrice.error = "Field can not be blank"
                return
            }
            if (stock == null) {
                binding.etStock.error = "Field can not be blank"
                return
            }
            if (desc.isEmpty()) {
                binding.etDesc.error = "Field can not be blank"
                return
            }

            ikan = Ikan(0, title, price, stock, desc)
            var intent = Intent()
            intent.putExtra(Register.EXTRA_USER, ikan)

            val values = ContentValues()
            values.put(DatabaseContract.IkanColumns.NAMA, title)
            values.put(DatabaseContract.IkanColumns.DESKRIPSI, desc)
            values.put(DatabaseContract.IkanColumns.HARGA, price)
            values.put(DatabaseContract.IkanColumns.STOCK, stock)
            val result = ikanHelper.insert(values)
            if (result > 0) {
                ikan?.id = result.toInt()
                setResult(Register.RESULT_ADD, intent)
                ikanHelper.close()
                Toast.makeText(this, "Berhasil menambah data ikan", Toast.LENGTH_SHORT).show()
//                intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}