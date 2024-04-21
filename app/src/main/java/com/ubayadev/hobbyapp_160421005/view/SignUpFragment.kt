package com.ubayadev.hobbyapp_160421005.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ubayadev.hobbyapp_160421005.R
import com.ubayadev.hobbyapp_160421005.databinding.FragmentSignUpBinding
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    private lateinit var binding:FragmentSignUpBinding
    private var queue: RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container,false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val username = binding.txtNewUsername.text.toString()
            val nama_depan = binding.txtNewNamaFirst.text.toString()
            val nama_belakang = binding.txtNewNamaLast.text.toString()
            val email = binding.txtNewEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val re_password = binding.txtRePass.text.toString()

            if(password == re_password) {
                signup(it, username, nama_depan, nama_belakang, email, password)
            } else
            {
                val dialog = AlertDialog.Builder(activity)
                dialog.setMessage("Cek apakah password yang dimasukkan ulang sudah sama.")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        }

        binding.btnBackLogin.setOnClickListener {
            val action = SignUpFragmentDirections.actionToLogin()
            Navigation.findNavController(view).navigate(action)
        }
    }

    fun signup(view:View, username:String, nama_depan:String, nama_belakang:String, email:String, password:String) {
        Log.d("signup", "signupVolley")

        queue = Volley.newRequestQueue(context)
        val url = "http://10.0.2.2/hobbyApp/signup.php"

        val dialog = AlertDialog.Builder(activity)

        val stringRequest = object:StringRequest(
            Request.Method.POST, url,
            {
                Log.d("cek", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    dialog.setMessage("Silakan login menggunakan username dan password yang baru didaftarkan.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        val action = SignUpFragmentDirections.actionToLogin()
                        Navigation.findNavController(view).navigate(action)
                    })
                } else {
                    dialog.setMessage("User gagal didaftarkan. Silakan coba lagi.")
                    dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    })
                    dialog.create().show()
                }
                dialog.create().show()
            },
            {
                Log.e("cekError", it.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["nama_depan"] = nama_depan
                params["nama_belakang"] = nama_belakang
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}