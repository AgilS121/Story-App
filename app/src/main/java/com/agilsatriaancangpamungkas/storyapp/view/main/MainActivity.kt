package com.agilsatriaancangpamungkas.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.agilsatriaancangpamungkas.storyapp.R
import com.agilsatriaancangpamungkas.storyapp.databinding.ActivityMainBinding
import com.agilsatriaancangpamungkas.storyapp.view.ViewModelFactory
import com.agilsatriaancangpamungkas.storyapp.view.addDataStory.AddDataStoryActivity
import com.agilsatriaancangpamungkas.storyapp.view.adapter.LoadingStateAdapter
import com.agilsatriaancangpamungkas.storyapp.view.adapter.MainAdapter
import com.agilsatriaancangpamungkas.storyapp.view.maps.MapsActivity
import com.agilsatriaancangpamungkas.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: MainAdapter
    private val mainViewModel by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStory.setOnClickListener { addDataStories() }

        setupView()
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

        val layoutManager = LinearLayoutManager(this)
        binding.tvRecycleview.layoutManager = layoutManager

        supportActionBar?.title = getString(R.string.app_name)

        showLoading(true)

        mainViewModel.getUser().observe(this) { it ->
            if (it.token.isEmpty()) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                mAdapter = MainAdapter()
                binding.tvRecycleview.adapter = mAdapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        mAdapter.retry()
                    }
                )

                mainViewModel.story().observe(this) {
                    mAdapter.submitData(lifecycle, it)
                    Toast.makeText(this,getString(R.string.have_data), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

            }
        }
    }

    private fun addDataStories() {
        startActivity(Intent(this, AddDataStoryActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                logoutConfirm()
                return true
            }
            R.id.settings -> {
                setupSettings()
                return true
            }
            R.id.map_action -> {
                mapAction()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSettings() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun logoutConfirm() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_confirm_logout))
        builder.setMessage(getString(R.string.question_logout))

        builder.setPositiveButton(getString(R.string.logout_yes)) { _, _ ->
            mainViewModel.logout()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton(getString(R.string.logout_no)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun mapAction() {
        startActivity(Intent(this, MapsActivity::class.java))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}