package com.testing.demo.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.testing.demo.R

class Util {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val v: View? = activity.window.currentFocus
            if (v != null) {
                val imm =
                    activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        fun showAlertDialog(
            context: Context?,
            msg: String,
            dialogHeaderTitle: String? = context?.getString(R.string.error)
        ) {
            AlertDialog.Builder(context)
                .setTitle(dialogHeaderTitle)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

        fun getFromPreference(activity: Activity, key: String): String? {
            val sharedPref: SharedPreferences =
                activity.getSharedPreferences(Constants.DEMO_PREFERENCE, Context.MODE_PRIVATE)
            return sharedPref.getString(key, Constants.EMPTY)
        }

        fun saveInPreference(activity: Activity, key: String, value: String?) {
            val sharedPref: SharedPreferences =
                activity.getSharedPreferences(Constants.DEMO_PREFERENCE, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun deleteAllFromPreference(activity: Activity) {
            activity.getSharedPreferences(Constants.DEMO_PREFERENCE, Context.MODE_PRIVATE).edit().clear()
                .apply()
        }
    }
}