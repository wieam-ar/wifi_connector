package com.example.wifi_connector

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    // how long the splash stays visible (ms)
    private val SPLASH_DELAY = 1200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // fade-in animation for the logo
        val logo = findViewById<ImageView>(R.id.splashLogo)
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 600
        fadeIn.fillAfter = true
        logo.startAnimation(fadeIn)

        // after delay open MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            // nice transition and finish splash so back won't return to it
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, SPLASH_DELAY)
    }
}
