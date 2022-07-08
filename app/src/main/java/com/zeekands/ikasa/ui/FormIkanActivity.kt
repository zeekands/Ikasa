package com.zeekands.ikasa.ui

import android.app.Activity
import android.content.*
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeekands.ikasa.MainActivity
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.R
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.databinding.ActivityFormIkanBinding
import com.zeekands.ikasa.db.*
import com.zeekands.ikasa.ui.home.ItemIkanAdapter
import com.zeekands.ikasa.ui.home.ItemPesananAdapter
import com.zeekands.ikasa.ui.register.Register
import com.zeekands.ikasa.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File


class FormIkanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormIkanBinding
    private lateinit var itemIkanAdapter: ItemIkanAdapter
    private lateinit var itemPesananAdapter: ItemPesananAdapter
    private lateinit var ikanHelper: IkanHelper
    private lateinit var transaksiHelper: TransaksiHelper
    private lateinit var loginHelper: LoginHelper
    private lateinit var imageUri : String
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

        imageUri = ""


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
    val REQUEST_CODE = 100
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

    fun getFilePath() : String{
        val wrapper = ContextWrapper(applicationContext);
        val imageDirectory = wrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        val file = File(imageDirectory, "image" + ".jpg");
        return file.path;
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }
                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    }
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, null, null,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}