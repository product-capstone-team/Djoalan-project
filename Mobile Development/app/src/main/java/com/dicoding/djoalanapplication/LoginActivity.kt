package com.dicoding.djoalanapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.djoalanapplication.data.user.User
import com.dicoding.djoalanapplication.databinding.ActivityLoginBinding
import com.dicoding.djoalanapplication.datastore.SessionViewModel
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.VMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var sessionViewModel: SessionViewModel
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthenticationViewModel by viewModels { VMFactory(this) }

    companion object {
        private const val ANIMATION_DURATION: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()

        binding.myButtonLoginCreateaccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.myButtonLogin.setOnClickListener {

            login()
        }

        viewModel.isLoading.observe(this) {
            showLoadingIcon(it)
        }
    }

    private fun playAnimation() {
        val imgLogo = ObjectAnimator.ofFloat(binding.imageViewLogin, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val emailText =
            ObjectAnimator.ofFloat(binding.labelLoginEmail, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val emailInput = ObjectAnimator.ofFloat(binding.inputEmailLogin, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION)
        val passwordText =
            ObjectAnimator.ofFloat(binding.labelLoginPassword, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.inputPasswordLogin, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val buttonLogin =
            ObjectAnimator.ofFloat(binding.myButtonLogin, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val tvCreateAcc =
            ObjectAnimator.ofFloat(binding.textViewCreateacc, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val buttonRegister =
            ObjectAnimator.ofFloat(binding.myButtonLoginCreateaccount, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)

        val together = AnimatorSet().apply {
            playTogether(buttonLogin,tvCreateAcc,buttonRegister)
        }
        AnimatorSet().apply {
            playSequentially(imgLogo,emailText,emailInput,passwordText,passwordInput,together)
            start()
        }
    }

    private fun showLoadingIcon(loadingState: Boolean) {
        if (loadingState) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }
    }

    private fun login() {
        val inputEmail = binding.inputEmailLogin.text.toString()
        val inputPassword = binding.inputPasswordLogin.text.toString()
        viewModel.login(inputEmail, inputPassword).observe(this) { res ->
            if (res != null) {

                if (res.error == false && res.data != null) {

                    res.data.checkEmail?.let {
                        val user = User(
                            it.id,
                            it.nama,
                            it.email,
                            it.password,
                            it.isBusinessAcc,
                            it.storeName,
                            it.company,
                            it.storeLocation?.lat,
                            it.storeLocation?.lon
                        )
                        sessionViewModel.saveUserState(true, it.isBusinessAcc)
                        viewModel.saveUserInfo(user)
                    }

                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                    Log.d("cek", "if -> ${res.error}")
                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("cek", "else -> ${res.data ?: "isNull"}")
                    Log.d("cek", "else -> ${res.error}")
                    Toast.makeText(this, res.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}