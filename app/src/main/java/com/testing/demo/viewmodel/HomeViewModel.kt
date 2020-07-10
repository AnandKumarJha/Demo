package com.testing.demo.viewmodel

import android.app.Activity
import android.net.Uri
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.testing.demo.R
import com.testing.demo.model.pojo.PostModel
import com.testing.demo.model.repository.HomeRepository

class HomeViewModel(val activity: Activity) : ViewModel() {
    var title = ObservableField<String>()
    var description = ObservableField<String>()
    var filePaths = ObservableArrayList<Uri>()
    var imageClicked = MutableLiveData<Boolean>()
    var isProcessRunning = ObservableBoolean()
    var showErrorMsg = MutableLiveData<String>()
    var homeRepository = HomeRepository(activity)

    fun onImageButtonClicked() {
        imageClicked.value = true
    }

    fun isDataValid(): Boolean {
        if (title.get().isNullOrEmpty()) {
            showErrorMsg.value = activity.getString(R.string.title_error)
            return false
        }
        if (description.get().isNullOrEmpty()) {
            showErrorMsg.value = activity.getString(R.string.description_error)
            return false
        }
        return true
    }

    suspend fun addPost(): MutableLiveData<Boolean> {
        isProcessRunning.set(true)
        return homeRepository.addPost(title.get(), description.get(), filePaths)
    }

    fun resetLayout() {
        title.set("")
        description.set("")
        filePaths.clear()
        isProcessRunning.set(false)
    }

    suspend fun getAllPosts(): MutableLiveData<ArrayList<PostModel>> {
        return homeRepository.getAllPosts()
    }

    suspend fun getMyPosts(): MutableLiveData<ArrayList<PostModel>> {
        return homeRepository.getMyPosts()
    }
}
