package com.testing.demo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.testing.demo.R

class ImageAdapter(
    private val imageList: ArrayList<String?>,
    val context: Context?
) : RecyclerView.Adapter<ImageAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var singleImage: ImageView = view.findViewById(R.id.single_image);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_image_of_image_row, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imageList.size - 1
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val image: String? = imageList.get(position + 1)
        Glide.with(context).load(image)
            .thumbnail(0.5f)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.singleImage)
    }

}