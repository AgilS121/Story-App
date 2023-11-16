package com.agilsatriaancangpamungkas.storyapp.view.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.agilsatriaancangpamungkas.storyapp.R
import com.agilsatriaancangpamungkas.storyapp.databinding.ActivityDetailStoryBinding
import com.agilsatriaancangpamungkas.storyapp.view.ViewModelFactory
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding
    private val detailViewModel by viewModels<DetailStoryViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detail_app)

        val storyID = intent.getStringExtra("story_id")

        detailViewModel.responseDetailStory.observe(this) { response ->
            val story = response.story

            val gambar = story?.photoUrl
            val judul = story?.name
            val deskripsi = story?.description

            if (gambar != null) {
                Glide.with(this).load(gambar).into(binding.ivDetailPhoto)
            }

            binding.tvDetailName.text = judul
            binding.tvDetailDescription.text = deskripsi
        }

        detailViewModel.getUser().observe(this) {response ->
            if (storyID != null) {
                detailViewModel.getDetailStories(storyID)
            } else {

                detailViewModel.errorMessage.observe(this) { message ->
                    message?.let {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    }
                }

                Log.d("tidakada id", "tidak ada id")
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}