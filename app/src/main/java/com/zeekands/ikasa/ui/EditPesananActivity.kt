package com.zeekands.ikasa.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.databinding.ActivityEditIkanBinding
import com.zeekands.ikasa.databinding.ActivityEditPesananBinding
import com.zeekands.ikasa.db.DatabaseContract
import com.zeekands.ikasa.db.IkanHelper
import com.zeekands.ikasa.db.TransaksiHelper
import com.zeekands.ikasa.db.UserHelper

class EditPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPesananBinding
    private lateinit var ikanHelper: IkanHelper
    private lateinit var userHelper: UserHelper
    private lateinit var transaksiHelper: TransaksiHelper

    companion object {
        const val ID = "id"
        const val ID_USER = "id"
        const val ID_IKAN = "id"
        const val BERAT = "berat"
        const val TOTAL = "total"
        const val STATUS = "status"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ikanHelper = IkanHelper.getInstance(this)
        ikanHelper.open()
        var cursor = ikanHelper.queryById(intent.getIntExtra(ID_IKAN, 0).toString())
        MappingHelper.mapIkanCursorToIkan(cursor).also {
            binding.tvIkan.text = it.nama
        }
        userHelper = UserHelper.getInstance(this)
        userHelper.open()
        cursor = userHelper.queryById(intent.getIntExtra(ID_USER, 0).toString())
        MappingHelper.mapUserCursorToUser(cursor)?.also {
            binding.tvNama.text = it.nama
        }
        transaksiHelper = TransaksiHelper.getInstance(this)
        transaksiHelper.open()

        binding.tvBerat.setText(intent.getIntExtra(BERAT, 0).toString())
        binding.tvTotal.setText(intent.getStringExtra(STATUS).toString())

        binding.btnAdd.setOnClickListener {
            val status = binding.etStatus.text.toString().trim()
            if (status.isEmpty()) {
                binding.etStatus.error = "Field can not be blank"
            }

            val values = ContentValues()
            values.put(DatabaseContract.TransaksiColumns.STATUS, status)
//            values.put(DatabaseContract.IkanColumns.DESKRIPSI, desc)
//            values.put(DatabaseContract.IkanColumns.HARGA, price)
//            values.put(DatabaseContract.IkanColumns.STOCK, stock)
            val result = transaksiHelper.update(intent.getIntExtra(ID, 0).toString(), values)
            if (result > 0) {
                Toast.makeText(this, "Berhasil mengubah status transaksi", Toast.LENGTH_SHORT).show()
                intent = Intent(this, FormIkanActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Gagal mengubah status transaksi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}