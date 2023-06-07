package com.dicoding.greenerizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.data.response.RubbishResponseItem
import com.dicoding.greenerizer.ui.articles.ArticlesFragmentDirections

class RubbishAdapter(private val listTrash: List<RubbishResponseItem>): RecyclerView.Adapter<RubbishAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTrash: TextView = view.findViewById(R.id.tv_trash)
        val imgTrash: ImageView = view.findViewById(R.id.img_trash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_trash, parent, false))
    }

    override fun getItemCount() = listTrash.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(listPhotoRubbish[position])
           .into(holder.imgTrash)
       holder.tvTrash.text = listTrash[position].jenisSampah

       holder.itemView.setOnClickListener {
           val toDetailRubbish = ArticlesFragmentDirections.actionNavigationArticlesToDetailArticleFragment(
               listPhotoRubbish[position],
               listTrash[position].jenisSampah.toString(),
               listTrash[position].deskripsiSampah.toString(),
               listTrash[position].hargaPerkiloSampah!!,
               listTrash[position].panduanPengelolaanSampah.toString()
           )
           it.findNavController().navigate(toDetailRubbish)
       }
    }

    private val listPhotoRubbish = listOf(
        "https://cdn.pixabay.com/photo/2019/10/25/01/52/cardboard-4575753_1280.jpg",
        "https://cdn.pixabay.com/photo/2014/01/31/06/42/glass-255281_1280.jpg",
        "https://cdn.pixabay.com/photo/2018/04/18/19/47/metal-3331407_1280.jpg",
        "https://cdn.pixabay.com/photo/2012/12/24/08/38/paper-72063_1280.jpg",
        "https://cdn.pixabay.com/photo/2019/01/29/13/26/plastic-waste-3962409_1280.jpg",
        "https://cdn.pixabay.com/photo/2018/10/09/21/08/foliage-3735937_1280.jpg"
    )
}