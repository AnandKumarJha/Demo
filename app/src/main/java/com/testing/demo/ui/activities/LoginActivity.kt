package com.testing.demo.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.testing.demo.R
import com.testing.demo.Utils.Constants
import com.testing.demo.Utils.UserData
import com.testing.demo.Utils.Util
import com.testing.demo.databinding.ActivityLoginBinding
import com.testing.demo.viewmodel.LoginViewModel
import com.testing.demo.viewmodel.factory.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserData.uid = Util.getFromPreference(this@LoginActivity, Constants.UID)
        UserData.phoneNumber = Util.getFromPreference(this@LoginActivity, Constants.PHONE_NUMBER)
        if (!UserData.uid.isNullOrEmpty()) {
            navigateToNewsFeedActivity()
        }

        val activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this@LoginActivity,
            R.layout.activity_login
        )
        viewModelFactory = ViewModelFactory(this)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        activityLoginBinding.loginViewModel = loginViewModel

        loginViewModel.toastMsgToBeDisplayed.observe(this, Observer { msg ->
            Util.hideKeyboard(this@LoginActivity)
            if (msg == "Success") {
                navigateToNewsFeedActivity()
            } else {
                if (msg.startsWith("Failure : ")) loginViewModel.isWaiting.set(false)
                Util.showAlertDialog(this@LoginActivity, msg)
            }
        })

        activityLoginBinding.countryCodePicker.setOnCountryChangeListener {
            loginViewModel.countryCode = activityLoginBinding.countryCodePicker.selectedCountryCode
        }
    }

    private fun navigateToNewsFeedActivity() {
        startActivity(Intent(this@LoginActivity, NewsFeedActivity::class.java))
        finish()
    }
}
