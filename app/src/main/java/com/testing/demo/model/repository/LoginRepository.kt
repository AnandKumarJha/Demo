package com.testing.demo.model.repository

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.testing.demo.Utils.Constants
import com.testing.demo.Utils.Util
import java.util.concurrent.TimeUnit

class LoginRepository(val activity: Activity, val toastMsgToBeDisplayed: MutableLiveData<String>) {
    private var TAG = "TAG";
    fun authenticateUser(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            activity, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks
    }


    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)
            toastMsgToBeDisplayed.value = "Failure : ${e.message}"
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val uid: String? = task.result?.user?.uid
                    val phoneNumber = task.result?.user?.phoneNumber
                    Util.saveInPreference(activity, Constants.UID, uid)
                    Util.saveInPreference(activity, Constants.PHONE_NUMBER, phoneNumber)
                    toastMsgToBeDisplayed.value = "Success"
                } else {
                    toastMsgToBeDisplayed.value = "Failure : ${task.exception}"
                }
            }
    }
}