package com.testing.demo.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.testing.demo.R
import com.testing.demo.Utils.Util
import com.testing.demo.databinding.PostFragmentBinding
import com.testing.demo.viewmodel.HomeViewModel
import com.testing.demo.viewmodel.factory.ViewModelFactory
import kotlinx.coroutines.*
import java.io.IOException

class PostFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null
    // request code
    private val PICK_IMAGE_REQUEST = 22
    private lateinit var imgContainer: LinearLayout
    private lateinit var btnSubmitPost: Button

    companion object {
        fun newInstance() = PostFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PostFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.post_fragment, container, false)
        imgContainer = binding.root.findViewById(R.id.img_container)
        btnSubmitPost = binding.root.findViewById(R.id.btn_submit_post)
        viewModelFactory = ViewModelFactory(this.requireActivity())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeVeiwModel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.imageClicked.observe(this.requireActivity(), Observer {
            selectImage()
        })

        viewModel.showErrorMsg.observe(this.requireActivity(), Observer { msg ->
            Util.showAlertDialog(context, msg)
        })

        btnSubmitPost.setOnClickListener {
            if (viewModel.isDataValid()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val postStatus = viewModel.addPost()
                    withContext(Dispatchers.Main) {
                        observePostStatus(postStatus)
                    }
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun observePostStatus(postStatus: MutableLiveData<Boolean>) {
        postStatus.observe(this.requireActivity(), Observer {
            if (it) {
                Util.showAlertDialog(
                    context,
                    getString(R.string.post_added),
                    getString(R.string.success)
                )
                viewModel.resetLayout()
                imgContainer.removeAllViews()
                val view =
                    layoutInflater.inflate(R.layout.single_image_of_image_row, null, false)
                view.layoutParams = LinearLayout.LayoutParams(170, 170)
                view.setOnClickListener {
                    selectImage()
                }
                imgContainer.addView(view)
            } else {
                Util.showAlertDialog(context, getString(R.string.something_went_wrong))
            }
        })
    }

    // Select Image method
    private fun selectImage() { // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    // Override onActivityResult method
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null
        ) { // Get the Uri of data
            filePath = data.data
            try { // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        activity?.contentResolver,
                        filePath
                    )
                viewModel.filePaths.add(filePath)
                val imageView = ImageView(context)
                imageView.layoutParams = LinearLayout.LayoutParams(170, 170)
                imageView.setImageBitmap(bitmap)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imgContainer.addView(imageView, 0)
            } catch (e: IOException) { // Log the exception
                e.printStackTrace()
            }
        }
    }
}
