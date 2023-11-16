package com.agilsatriaancangpamungkas.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.agilsatriaancangpamungkas.storyapp.databinding.ActivityLoginBinding
import com.agilsatriaancangpamungkas.storyapp.view.main.MainActivity
import com.agilsatriaancangpamungkas.storyapp.view.ViewModelFactory
import com.agilsatriaancangpamungkas.storyapp.view.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        lAnimation()
    }

    private fun setupView() {
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

        authViewModel.showLoading.observe(this) {
            showLoading(it)
        }

        authViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.responseLogin.observe(this) { login ->
            if (login.error == true) {
                Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            lifecycleScope.launch {
                authViewModel.login(email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun lAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val button = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTXT, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.messageTXT, View.ALPHA, 1f).setDuration(100)
        val txtEmail = ObjectAnimator.ofFloat(binding.emailTXT, View.ALPHA, 1f).setDuration(100)
        val textInputEmail = ObjectAnimator.ofFloat(binding.emailEdtLayout, View.ALPHA, 1f).setDuration(100)
        val txtPassword = ObjectAnimator.ofFloat(binding.passwordTXT, View.ALPHA, 1f).setDuration(100)
        val textInputPassword = ObjectAnimator.ofFloat(binding.passwordEdtLayout, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, desc, txtEmail, textInputEmail, txtPassword, textInputPassword, button)
            start()
        }
    }
}