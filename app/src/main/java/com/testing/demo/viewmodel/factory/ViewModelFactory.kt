package com.testing.demo.viewmodel.factory

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.testing.demo.viewmodel.HomeViewModel
import com.testing.demo.viewmodel.LoginViewModel

class ViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(activity) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(activity) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}