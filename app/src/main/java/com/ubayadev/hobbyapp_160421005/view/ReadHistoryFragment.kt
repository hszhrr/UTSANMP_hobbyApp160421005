package com.ubayadev.hobbyapp_160421005.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ubayadev.hobbyapp_160421005.R
import com.ubayadev.hobbyapp_160421005.databinding.FragmentReadHistoryBinding
import com.ubayadev.hobbyapp_160421005.databinding.FragmentSignUpBinding

class ReadHistoryFragment : Fragment() {
    private lateinit var binding:FragmentReadHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadHistoryBinding.inflate(inflater, container,false)
        return (binding.root)
    }

}