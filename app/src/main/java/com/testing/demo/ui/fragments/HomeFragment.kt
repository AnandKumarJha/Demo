package com.testing.demo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testing.demo.R
import com.testing.demo.model.pojo.PostModel
import com.testing.demo.ui.adapter.PostAdapter
import com.testing.demo.viewmodel.HomeViewModel
import com.testing.demo.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var rvPosts: RecyclerView
    private lateinit var listPost: ArrayList<PostModel>
    private lateinit var mAdapter: PostAdapter
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        rvPosts = view.findViewById(R.id.rvPosts) as RecyclerView
        listPost = ArrayList()
        mAdapter = PostAdapter(listPost, context)
        val mLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context)
        rvPosts.layoutManager = mLayoutManager
        rvPosts.itemAnimator = DefaultItemAnimator()
        rvPosts.adapter = mAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = ViewModelFactory(this.requireActivity())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            val posts = viewModel.getAllPosts()
            withContext(Dispatchers.Main) {
                observeData(posts)
            }
        }

    }

    private fun observeData(posts: MutableLiveData<ArrayList<PostModel>>) {
        posts.observe(this.requireActivity(), Observer {
            listPost.addAll(it)
            mAdapter.notifyDataSetChanged()
        })
    }

}
