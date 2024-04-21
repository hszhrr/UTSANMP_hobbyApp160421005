package com.ubayadev.hobbyapp_160421005.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubayadev.hobbyapp_160421005.R
import com.ubayadev.hobbyapp_160421005.databinding.FragmentBeritaDetailBinding
import com.ubayadev.hobbyapp_160421005.databinding.FragmentProfilBinding
import com.ubayadev.hobbyapp_160421005.model.User
import com.ubayadev.hobbyapp_160421005.view.SignInActivity.Companion.currUserId
import org.json.JSONObject


class ProfilFragment : Fragment() {
    private lateinit var binding:FragmentProfilBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("getData", "getDataVolley")
        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/hobbyApp/get_user.php"

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val userData = data.getJSONObject(0)
                        val sType = object : TypeToken<User>() {}.type
                        val user = Gson().fromJson(userData.toString(), sType) as User
                        binding.txtNamaDepan.setText(user.nama_depan)
                        binding.txtNamaBelakang.setText(user.nama_belakang)
                        binding.txtNewPassword.setText(user.password)

                        binding.btnSimpan.setOnClickListener {
                            val newNamaDepan = binding.txtNamaDepan.text.toString()
                            val newNamaBelakang = binding.txtNamaBelakang.text.toString()
                            val newPassword = binding.txtNewPassword.text.toString()
                            updateData(user, newNamaDepan, newNamaBelakang, newPassword)
                        }
                    }}
                },
                {
                    Log.e("error", "errorVolley")
                }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["id"] = currUserId.toString()
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)

        binding.btnLogout.setOnClickListener {
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    fun updateData(user:User, newNamaDepan:String, newNamaBelakang:String, newPassword:String) {

        Log.d("updateData", "updateDataVolley")
        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/hobbyApp/update_data.php"

        val dialog = AlertDialog.Builder(activity)

        val stringRequest = object:StringRequest(
            Request.Method.POST, url, {
                Log.d("cek", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    dialog.setMessage("Data berhasil diubah.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    dialog.create().show()
                } else {
                    dialog.setMessage("Data gagal diubah. \nSilakan coba lagi.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    dialog.create().show()
                }

            }, {
                dialog.setMessage("Data gagal diubah. \nSilakan coba lagi.")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }

        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["id"] = user.id.toString()
                params["nama_depan"] = newNamaDepan.toString()
                params["nama_belakang"] = newNamaBelakang.toString()
                params["password"] = newPassword.toString()
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}