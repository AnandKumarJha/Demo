package com.testing.demo.viewmodel

import android.app.Activity
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.testing.demo.model.repository.LoginRepository

class LoginViewModel(activity: Activity) : ViewModel() {
    var mobileNumber: String? = ""
    var otpText: String? = ""
    var toastMsgToBeDisplayed = MutableLiveData<String>()
    var countryCode : String? = "91"
    var repository : LoginRepository = LoginRepository(activity, toastMsgToBeDisplayed)
    val isWaiting = ObservableBoolean()

    fun loginButtonClicked() {
        if (mobileNumber!!.isEmpty() || mobileNumber!!.length < 10) {
            toastMsgToBeDisplayed.value = "Please enter a correct mobile number"
            return
        } else {
            isWaiting.set(true)
            repository.authenticateUser("+$countryCode$mobileNumber")
        }
    }
}