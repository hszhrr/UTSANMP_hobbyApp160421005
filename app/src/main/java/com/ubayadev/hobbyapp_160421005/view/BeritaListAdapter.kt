package com.ubayadev.hobbyapp_160421005.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.ubayadev.hobbyapp_160421005.databinding.BeritaListItemBinding
import com.ubayadev.hobbyapp_160421005.model.Berita

class BeritaListAdapter (val beritaList:ArrayList<Berita>)
    : RecyclerView.Adapter<BeritaListAdapter.BeritaViewHolder>(){

    class BeritaViewHolder(var binding: BeritaListItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaViewHolder {
        val binding = BeritaListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return BeritaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BeritaViewHolder, position: Int) {
        holder.binding.txtJudul.text = beritaList[position].judul
        holder.binding.txtCreator.text = beritaList[position].kreator
        holder.binding.txtRingkasan.text = beritaList[position].ringkasan

        val picasso = Picasso.Builder(holder.itemView.context)
        picasso.listener { picasso, uri, exception -> exception.printStackTrace() }
        picasso.build().load(beritaList[position].img_url)
            .into(holder.binding.imgBerita, object :
                Callback {
                override fun onSuccess() {
                    holder.binding.progressImg.visibility = View.INVISIBLE
                    holder.binding.imgBerita.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    Log.e("picasso_error", e.toString())
                }
            })
        holder.binding.btnRead.setOnClickListener {
            val beritaId = beritaList[position].id!!.toString()!!
            val action = BeritaListFragmentDirections.actionBeritaDetail(beritaId)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
       return beritaList.size
    }

    fun updateBeritaList(newBeritaList: ArrayList<Berita>) {
        beritaList.clear()
        beritaList.addAll(newBeritaList)
        notifyDataSetChanged()
    }
}