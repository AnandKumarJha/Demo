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
import androidx.recyclerview.widget.RecyclerView
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.testing.demo.R
import com.testing.demo.model.pojo.PostModel
import com.testing.demo.ui.adapter.ImageAdapter
import com.testing.demo.viewmodel.HomeViewModel
import com.testing.demo.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.Runnable


class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var rvProfilePic: RecyclerView
    private lateinit var mAdapter: ImageAdapter
    private lateinit var listImages: ArrayList<String?>
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        rvProfilePic = view.findViewById(R.id.rvProfilePic) as RecyclerView
        listImages = ArrayList()
        mAdapter = ImageAdapter(listImages, context)

        val spannedGridLayoutManager = SpannedGridLayoutManager(
            orientation = SpannedGridLayoutManager.Orientation.VERTICAL,
            spans = 3
        )
        spannedGridLayoutManager.itemOrderIsStable = true
        spannedGridLayoutManager.spanSizeLookup =
            SpannedGridLayoutManager.SpanSizeLookup { position ->
                when {
                    position % 7 == 1 ->
                        SpanSize(2, 2)
                    else ->
                        SpanSize(1, 1)
                }
            }
        rvProfilePic.layoutManager = spannedGridLayoutManager
        rvProfilePic.itemAnimator = DefaultItemAnimator()
        rvProfilePic.adapter = mAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelFactory = ViewModelFactory(this.requireActivity())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val posts = viewModel.getMyPosts()
                withContext(Dispatchers.Main){
                    observeData(posts)
                }
            }catch (e:Exception){
                //to catch the exception thrown explicitly
                e.printStackTrace()
            }

        }
    }

    private fun observeData(posts: MutableLiveData<ArrayList<PostModel>>) {
        posts.observe(this.requireActivity(), Observer {
            for (post in it) {
                listImages.addAll(post.profilePics)
            }
            mAdapter.notifyDataSetChanged()
            rvProfilePic.post(Runnable { rvProfilePic.smoothScrollToPosition(0) })
        })
    }
}
