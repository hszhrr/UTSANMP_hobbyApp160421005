package com.ubayadev.hobbyapp_160421005.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ubayadev.hobbyapp_160421005.R
import com.ubayadev.hobbyapp_160421005.databinding.FragmentLoginBinding
import com.ubayadev.hobbyapp_160421005.model.User
import com.ubayadev.hobbyapp_160421005.view.SignInActivity.Companion.currUserId
import org.json.JSONObject


class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    private var queue:RequestQueue? = null
    val TAG = "volleyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.txtUsername.text.toString()
            val password = binding.txtPass.text.toString()
            login(username, password)
        }

        binding.btnCreateAcc.setOnClickListener {
            val action = LoginFragmentDirections.actionToSignUp()
            Navigation.findNavController(it).navigate(action)
        }
    }

    fun login(username:String, password:String){
        Log.d("login", "loginVolley")

        queue = Volley.newRequestQueue(activity)
        val url = "http://10.0.2.2/hobbyApp/cek_login.php"
        val dialog = AlertDialog.Builder(activity)

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            {
                Log.d("apiresult", it)
                val obj = JSONObject(it)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONArray("data")
                    if (data.length() > 0) {
                        val userData = data.getJSONObject(0)
                        val sType = object : TypeToken<User>() { }.type
                        val user = Gson().fromJson(userData.toString(), sType) as User
                        dialog.setMessage("Login Successful, Welcome ${user.username}")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener {
                            dialog, which ->
                            dialog.dismiss()
                            SignInActivity.currUserId = user.id!!
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        })
                        dialog.create().show()
                    } else {
                        dialog.setMessage("Username or Password is incorrect")
                        dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
                        dialog.create().show()
                    }
                }
            },
            {
                Log.e("cekError", it.toString())
                dialog.setMessage("Username or Password is incorrect")
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                dialog.create().show()
            }
        ) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }

}