package com.rak12.mod3app.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.rak12.mod3app.R
import com.rak12.mod3app.fragment.*

class DashboardActivity : AppCompatActivity() {
    lateinit var dl: DrawerLayout
    lateinit var nv: NavigationView
    lateinit var sp: SharedPreferences
    lateinit var fl: FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var cl: CoordinatorLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dl = findViewById(R.id.drawer)
        nv = findViewById(R.id.navigationdrawer)
        fl = findViewById(R.id.frame)
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        cl = findViewById(R.id.cord)
        openhome()
        var previousitem: MenuItem? = null
        setuptoolbar()


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this, dl,
            R.string.opendrawer,
            R.string.closedrawer
        )
        dl.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        nv.setNavigationItemSelectedListener {
            if (previousitem != null) {
                previousitem?.isChecked = false

            }
            it.isCheckable = true
            it.isChecked = true
            previousitem = it

            when (it.itemId) {
                R.id.home -> {
                    openhome()
                    dl.closeDrawers()
                }
                R.id.prof -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Profile())
                        .commit()
                    supportActionBar?.title = "My Profile"
                    dl.closeDrawers()
                }
                R.id.fav -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Favourites())
                        .commit()
                    supportActionBar?.title = "My Favourites"
                    dl.closeDrawers()
                }
                R.id.Faq -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Faq()).commit()
                    supportActionBar?.title = "Faq's"
                    dl.closeDrawers()
                }
                R.id.history -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame, Orderhistory())
                        .commit()
                    supportActionBar?.title = "Order History"
                    dl.closeDrawers()

                }
                R.id.logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            sp.edit().clear().apply()
                            startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                            Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                            ActivityCompat.finishAffinity(this)

                        }
                        .setNegativeButton("No") { _, _ ->
                            openhome()
                            dl.closeDrawers()
                        }
                        .create()
                        .show()

                }
            }

            return@setNavigationItemSelectedListener true
        }
        val convertView = LayoutInflater.from(this@DashboardActivity).inflate(R.layout.header, null)
        val userName: TextView = convertView.findViewById(R.id.tx1)
        val userPhone: TextView = convertView.findViewById(R.id.txt2)
        userName.text = sp.getString("name", null)
        val phoneText = "+91-${sp.getString("mobile_number", null)}"
        userPhone.text = phoneText
        nv.addHeaderView(convertView)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            dl.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openhome() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, Home()).commit()
        nv.setCheckedItem(R.id.home)
        supportActionBar?.title = "All Restaurants"
    }


    fun setuptoolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is Home -> openhome()
            else -> {
                ActivityCompat.finishAffinity(this@DashboardActivity)
            }
        }


    }
}