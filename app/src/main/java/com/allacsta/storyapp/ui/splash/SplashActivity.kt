package com.allacsta.storyapp.ui.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.allacsta.storyapp.R
import com.allacsta.storyapp.ui.ViewModelFactory
import com.allacsta.storyapp.ui.main.MainActivity
import com.allacsta.storyapp.ui.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = obtainViewModel(this)

        hideSystemUI()

        Handler().postDelayed({
            getUserSession()
        }, 2000)
    }

    private fun getUserSession() {
        viewModel.getSession().observe(this){ user ->
            if (!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): SplashViewModel {
        val factory = ViewModelFactory.getInstance(activity.application, true)
        return ViewModelProvider(activity, factory).get(SplashViewModel::class.java)
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}