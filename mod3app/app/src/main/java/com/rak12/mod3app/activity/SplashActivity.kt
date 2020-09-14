package com.rak12.mod3app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.rak12.mod3app.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Food Runer"
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }, 1000)
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }
}