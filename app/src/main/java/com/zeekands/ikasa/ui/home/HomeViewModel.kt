package com.zeekands.ikasa.ui.home

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeekands.ikasa.MappingHelper
import com.zeekands.ikasa.data.Ikan
import com.zeekands.ikasa.db.IkanHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val dataIkan = MutableLiveData<ArrayList<Ikan>>()

    fun getDataIkan(ikanHelper: IkanHelper) {
        viewModelScope.launch {
            ikanHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = ikanHelper.queryAll()
                MappingHelper.mapIkanCursorToArrayList(cursor)
            }
            val ikans = deferredNotes.await()
            if (ikans.size > 0) {
                dataIkan.postValue(ikans)
            }
            ikanHelper.close()
        }
    }
}