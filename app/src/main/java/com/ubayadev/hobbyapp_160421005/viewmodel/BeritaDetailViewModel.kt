package com.ubayadev.hobbyapp_160421005.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubayadev.hobbyapp_160421005.model.Berita

class BeritaDetailViewModel(application: Application): AndroidViewModel(application)  {
    val beritaLD = MutableLiveData<Berita>()
    val beritaLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun fetch(beritaId:Int) {
        beritaLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/hobbyApp/beritas.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                Log.d("showvolley", it)
                val sType = object : TypeToken<List<Berita>>() { }.type
                val result = Gson().fromJson<List<Berita>>(it, sType)
                val beritaList = result as ArrayList<Berita>
                beritaLD.value = beritaList[beritaId - 1]
                loadingLD.value = false

                Log.d("showvolley", result.toString())
            },
            {
                Log.d("showvolley", it.toString())
                beritaLoadErrorLD.value = false
                loadingLD.value = false
            })

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}