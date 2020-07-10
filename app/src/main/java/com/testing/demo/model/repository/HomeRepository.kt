package com.testing.demo.model.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.testing.demo.Utils.UserData
import com.testing.demo.model.pojo.PostModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeRepository(val activity: Activity) {
    // instance for firebase storage and StorageReference
    var imageList = ArrayList<String?>()
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageReference: StorageReference = storage.reference
    var database = FirebaseDatabase.getInstance().reference
    var dataSentToServer = MutableLiveData<Boolean>()
    var imageSelectedCount: Int = 0
    var allPost = MutableLiveData<ArrayList<PostModel>>()


   fun addPost(
        title: String?,
        description: String?,
        imagePath: ArrayList<Uri>
    ): MutableLiveData<Boolean> {
        imageList.clear()
        imageSelectedCount = imagePath.size
        if (imageSelectedCount > 0) {
            for (image in imagePath) {
                uploadImage(image, title, description)
            }
        } else {
            sendData(title, description, imageList)
        }
        return dataSentToServer
    }

    fun getAllPosts(): MutableLiveData<ArrayList<PostModel>> {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                throw Exception("Data fetching cancelled")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = ArrayList<PostModel>()
                for (singlePost in snapshot.children) {
                    postList.add(singlePost.getValue(PostModel::class.java) as PostModel)
                }
                allPost.value = postList
            }
        })
        return allPost
    }

    fun getMyPosts(): MutableLiveData<ArrayList<PostModel>> {
        database.orderByChild("createdBy").equalTo(UserData.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    throw Exception("Data fetching cancelled")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = ArrayList<PostModel>()
                    for (singlePost in snapshot.children) {
                        postList.add(singlePost.getValue(PostModel::class.java) as PostModel)
                    }
                    allPost.value = postList
                }
            })
        return allPost
    }

    private fun uploadImage(
        image: Uri,
        title: String?,
        description: String?
    ) {
        val ref: StorageReference = storageReference.child(
            "images/" + UUID.randomUUID().toString()
        )
        ref.putFile(image).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                imageList.add(downloadUri?.toString())
                if (imageList.size == imageSelectedCount) {
                    sendData(title, description, imageList)
                }
            } else {
                Log.e("TAG", "Failed")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendData(title: String?, description: String?, imagePath: ArrayList<String?>) {
        val key = database.push().key.toString()
        val dateFormat = SimpleDateFormat("dd/MMM/YYYY")
        database.child(key).setValue(
            PostModel(
                title, description, UserData.uid,
                dateFormat.format(Date()), imagePath, UserData.phoneNumber
            )
        ).addOnSuccessListener {
            dataSentToServer.value = true
        }.addOnFailureListener {
            dataSentToServer.value = false
        }
    }
}