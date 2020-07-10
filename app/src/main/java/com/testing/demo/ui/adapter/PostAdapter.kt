package com.testing.demo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.testing.demo.R
import com.testing.demo.model.pojo.PostModel

class PostAdapter(
    private val postList: ArrayList<PostModel>,
    val context: Context?
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView = view.findViewById(R.id.tv_title)
        var tvDescription: TextView = view.findViewById(R.id.tv_description)
        var tvCreatedBy: TextView = view.findViewById(R.id.tv_created_by)
        var tvCreatedOn: TextView = view.findViewById(R.id.tv_created_on)
        var ivMain: ImageView = view.findViewById(R.id.iv_main);
        var recyclerView: RecyclerView = view.findViewById(R.id.post_img_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postList.size;
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: PostModel = postList[position]
        holder.tvTitle.text = post.title
        holder.tvDescription.text = post.description
        holder.tvCreatedOn.text = post.createdOn
        holder.tvCreatedBy.text = post.phoneNumber
        if (post.profilePics.size > 0) {
            Glide.with(context).load(post.profilePics[0])
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivMain)
        }
        if (post.profilePics.size > 1) {
            val mAdapter = ImageAdapter(post.profilePics, context)
            val mLayoutManager: RecyclerView.LayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            holder.recyclerView.layoutManager = mLayoutManager
            holder.recyclerView.itemAnimator = DefaultItemAnimator()
            holder.recyclerView.adapter = mAdapter
        } else {
            holder.recyclerView.visibility = View.GONE
        }
    }

}