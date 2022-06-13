package com.dicoding.djoalanapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.djoalanapplication.databinding.ActivityRegisterBinding
import com.dicoding.djoalanapplication.ui.AuthenticationViewModel
import com.dicoding.djoalanapplication.ui.VMFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthenticationViewModel by viewModels { VMFactory(this) }

    companion object {
        private const val ANIMATION_DURATION: Long = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        playAnimation()

        binding.registerButton.setOnClickListener {
            register()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }
    }

    private fun playAnimation() {
        val imglogo =
            ObjectAnimator.ofFloat(binding.imageViewRegister, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val nameText =
            ObjectAnimator.ofFloat(binding.labelRegisterName, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val nameInput = ObjectAnimator.ofFloat(binding.editRegisterName, View.ALPHA, 1f)
            .setDuration(ANIMATION_DURATION)
        val emailText =
            ObjectAnimator.ofFloat(binding.labelRegisterEmail, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val emailInput =
            ObjectAnimator.ofFloat(binding.inputEmailRegister, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val passwordText =
            ObjectAnimator.ofFloat(binding.labelRegisterPassword, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val passwordInput =
            ObjectAnimator.ofFloat(binding.inputPasswordRegister, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)
        val buttonRegister =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(ANIMATION_DURATION)

        AnimatorSet().apply {
            playSequentially(imglogo,nameText,nameInput,emailText,emailInput,passwordText,passwordInput, buttonRegister)
            start()
        }
    }

    private fun register() {
        val inputName = binding.editRegisterName.text.toString()
        val inputEmail = binding.inputEmailRegister.text.toString()
        val inputPassword = binding.inputPasswordRegister.text.toString()

        viewModel.register(inputName, inputEmail, inputPassword).observe(this) {
            if (it.error == false) {
                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
            }
        }.also {
            binding.apply {
                editRegisterName.setText("")
                inputEmailRegister.setText("")
                inputPasswordRegister.setText("")
            }
        }
    }
}