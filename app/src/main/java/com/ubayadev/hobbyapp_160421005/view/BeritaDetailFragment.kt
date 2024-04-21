package com.ubayadev.hobbyapp_160421005.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.ubayadev.hobbyapp_160421005.R
import com.ubayadev.hobbyapp_160421005.databinding.FragmentBeritaDetailBinding
import com.ubayadev.hobbyapp_160421005.viewmodel.BeritaDetailViewModel


class BeritaDetailFragment : Fragment() {
    private lateinit var detailViewModel: BeritaDetailViewModel
    private lateinit var binding:FragmentBeritaDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBeritaDetailBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beritaId = BeritaDetailFragmentArgs.fromBundle(requireArguments()).beritaId

        detailViewModel = ViewModelProvider(this).get(BeritaDetailViewModel::class.java)
        detailViewModel.fetch(beritaId.toInt())

        observeViewModel()
    }

    fun observeViewModel() {
        detailViewModel.beritaLD.observe(viewLifecycleOwner, Observer {
            binding.txtJudul.text = it.judul
            binding.txtCreator.text = it.kreator

            var currPar  = 0
            val splitPar = it.isi?.split("\n")
            Log.d("cekParagraf", splitPar.toString())
            val par_size = splitPar?.size

            binding.txtParagraf.text = splitPar?.get(currPar)
            cekPage(currPar, par_size!!.toInt())

            Picasso.get().load(it.img_url).into(binding.imgBerita)
            var berita = it

            binding.btnNext.setOnClickListener() {
                currPar++
                binding.txtParagraf.text = splitPar!!.get(currPar)
                cekPage(currPar, par_size)
            }

            binding.btnPrev.setOnClickListener() {
                currPar--
                binding.txtParagraf.text = splitPar!!.get(currPar)
                cekPage(currPar, par_size)
            }
            })
        detailViewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.progressImage.visibility = View.VISIBLE
            } else {
                binding.progressImage.visibility = View.GONE
            }
        })
    }

    fun cekPage(currpar:Int, par_size:Int) {
        when(currpar) {
            0 ->  {
                binding.btnPrev.isEnabled = false
                binding.btnNext.isEnabled = true
            }
            par_size - 1 -> {
                binding.btnPrev.isEnabled = true
                binding.btnNext.isEnabled = false
            }
            else -> {
                binding.btnPrev.isEnabled = true
                binding.btnNext.isEnabled = true
            }
        }

    }
}