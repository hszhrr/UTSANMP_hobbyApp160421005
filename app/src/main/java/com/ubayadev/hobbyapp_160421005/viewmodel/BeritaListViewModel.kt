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

class BeritaListViewModel(application: Application): AndroidViewModel(application) {
    val beritasLD = MutableLiveData<ArrayList<Berita>>()
    val beritasLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    fun refresh() {
        beritasLoadErrorLD.value = false
        loadingLD.value = false

        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/hobbyApp/beritas.json"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {
                val sType = object : TypeToken<List<Berita>>() { }.type
                val result = Gson().fromJson<List<Berita>>(it, sType)
                beritasLD.value = result as ArrayList<Berita>?
                loadingLD.value = false
                Log.d("showvolley", result.toString())
            },
            {
                Log.d("showvolley", it.toString())
                beritasLoadErrorLD.value = false
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