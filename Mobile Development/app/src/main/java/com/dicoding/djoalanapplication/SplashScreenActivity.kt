package com.dicoding.djoalanapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dicoding.djoalanapplication.databinding.ActivitySplashScreenBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_TIME_OUT: Long = 2500
    }

    @Inject lateinit var sessionViewModel: SessionViewModel
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sessionViewModel.getUserAccType().observe(this) { isBusiness ->
            if (isBusiness) {
                binding.textTagline.text = getString(R.string.tagline)
            }
        }

        sessionViewModel.getUserStateLogin().observe(this) {isLogin ->
            if (!isLogin) {
                Handler().postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }, SPLASH_TIME_OUT)
            } else {
                Handler().postDelayed({
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }, SPLASH_TIME_OUT)
            }
        }
    }


}