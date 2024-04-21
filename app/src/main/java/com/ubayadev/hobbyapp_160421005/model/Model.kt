package com.ubayadev.hobbyapp_160421005.model

import com.google.gson.annotations.SerializedName

data class User(
    val id:Int?,
    val username:String?,
    val nama_depan:String?,
    val nama_belakang:String?,
    val email:String?,
    val password:String?
)

data class Berita(
    val id:String?,
    val judul:String?,
    val kreator:String?,
    val img_url:String?,
    val ringkasan:String?,
    val isi:String?
)