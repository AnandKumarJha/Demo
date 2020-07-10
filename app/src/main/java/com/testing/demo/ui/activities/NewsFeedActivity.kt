package com.testing.demo.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.testing.demo.R
import com.testing.demo.Utils.Util
import com.testing.demo.ui.fragments.HomeFragment
import com.testing.demo.ui.fragments.PostFragment
import com.testing.demo.ui.fragments.ProfileFragment


class NewsFeedActivity : AppCompatActivity() {
    private var toolbar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_feed)
        toolbar = supportActionBar
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        toolbar?.title = getString(R.string.home)
        loadFragment(HomeFragment.newInstance())
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_home -> {
                        toolbar!!.setTitle(getString(R.string.home))
                        loadFragment(HomeFragment.newInstance())
                        return true
                    }
                    R.id.menu_post -> {
                        toolbar!!.setTitle(getString(R.string.post))
                        loadFragment(PostFragment.newInstance())
                        return true
                    }
                    R.id.menu_profile -> {
                        toolbar!!.setTitle(getString(R.string.profile))
                        loadFragment(ProfileFragment.newInstance())
                        return true
                    }
                }
                return false
            }
        }

    private fun loadFragment(fragment: Fragment) { // load fragment
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_header_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                Toast.makeText(this@NewsFeedActivity, "Setting clicked", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.menu_logout -> {
                AlertDialog.Builder(this@NewsFeedActivity)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(
                        R.string.yes
                    ) { dialog, _ ->
                        dialog.dismiss()
                        FirebaseAuth.getInstance().signOut();
                        Util.deleteAllFromPreference(this@NewsFeedActivity)
                        startActivity(Intent(this@NewsFeedActivity, LoginActivity::class.java))
                        finish()
                    }
                    .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                    .show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
