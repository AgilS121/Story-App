package com.agilsatriaancangpamungkas.storyapp.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.agilsatriaancangpamungkas.storyapp.databinding.ActivityRegisterBinding
import com.agilsatriaancangpamungkas.storyapp.view.ViewModelFactory
import com.agilsatriaancangpamungkas.storyapp.view.login.LoginActivity
import com.agilsatriaancangpamungkas.storyapp.view.viewModel.AuthViewModel
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel by viewModels<AuthViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        rAnimation()
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

        authViewModel.responseRegister.observe(this) {  regis ->
            if (regis.error == true) {
                Toast.makeText(this, "Ada Kesalahan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sukses, Anda Telah terdaftar", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        authViewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            lifecycleScope.launch {
                authViewModel.register(name, email, password)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun rAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewReg, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val button = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTXT, View.ALPHA, 1f).setDuration(100)
        val desc = ObjectAnimator.ofFloat(binding.messageTXT, View.ALPHA, 1f).setDuration(100)
        val txtName = ObjectAnimator.ofFloat(binding.nameTXT, View.ALPHA, 1f).setDuration(100)
        val textInputName = ObjectAnimator.ofFloat(binding.nameEdtLayout, View.ALPHA, 1f).setDuration(100)
        val txtEmail = ObjectAnimator.ofFloat(binding.emailTXTReg, View.ALPHA, 1f).setDuration(100)
        val textInputEmail = ObjectAnimator.ofFloat(binding.emailEdtLayout, View.ALPHA, 1f).setDuration(100)
        val txtPassword = ObjectAnimator.ofFloat(binding.passwordTXT, View.ALPHA, 1f).setDuration(100)
        val textInputPassword = ObjectAnimator.ofFloat(binding.passwordEdtLayout, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, desc,txtName, textInputName, txtEmail, textInputEmail, txtPassword, textInputPassword, button)
            start()
        }
    }


}