package com.agilsatriaancangpamungkas.storyapp.view.addDataStory

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.agilsatriaancangpamungkas.storyapp.R
import com.agilsatriaancangpamungkas.storyapp.databinding.ActivityAddDataStoryBinding
import com.agilsatriaancangpamungkas.storyapp.utils.getImgUri
import com.agilsatriaancangpamungkas.storyapp.utils.reduceFileImage
import com.agilsatriaancangpamungkas.storyapp.utils.uriToFile
import com.agilsatriaancangpamungkas.storyapp.view.ViewModelFactory
import com.agilsatriaancangpamungkas.storyapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddDataStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDataStoryBinding
    private var currentImgUri: Uri? = null
    private var locationClient: FusedLocationProviderClient? = null
    private var lastKnownLocation: Location? = null

    private val adViewModel by viewModels<AddDataViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_app)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.galleryBtn.setOnClickListener { gallery() }
        binding.cameraBtn.setOnClickListener { camera() }
        binding.buttonAdd.setOnClickListener { uploadImg() }
        binding.myCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestLocation()
            }
        }
    }

    private fun gallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun camera() {
        currentImgUri = getImgUri(this)
        launcherCamera.launch(currentImgUri)
    }

    private fun uploadImg() {
        currentImgUri?.let { uri ->
            val imgFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File Exist", "show Image ${imgFile.path}")
            val description = binding.edAddDescription.text.toString()

            adViewModel.showLoading.observe(this) {
                showLoading(it)
            }

            val reqBody = description.toRequestBody("text/plain".toMediaType())
            val reqImgFile = imgFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imgFile.name,
                reqImgFile
            )

            val lat = lastKnownLocation?.latitude?.toFloat()
            val lon = lastKnownLocation?.longitude?.toFloat()

            adViewModel.getUser().observe(this) {response ->
                Log.d("iniresponse add data", "$response, $reqBody")
                adViewModel.addDataStories(multipartBody, reqBody, lat, lon)
            }

            adViewModel.responseAddStories.observe(this) {response ->
                Log.d("iniresponse add data 2", "$response")
                if (response.error == true) {
                    response.message?.let { showToast(it) }
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }

            adViewModel.errorMessage.observe(this) { message ->
                message?.let {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                }
            }

        } ?: showToast(getString(R.string.image_warning))
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            if (permission.all { it.value} ) {
                requestLocation()
            } else {
                showToast(getString(R.string.permission))
            }
        }

    private fun requestLocation() {
        val permissionsToRequest = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (ContextCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
            ) {
            locationClient?.lastLocation?.addOnSuccessListener(this) { location: Location? ->
                if (location != null) {
                    lastKnownLocation = location
                } else {
                    showToast(getString(R.string.unknowlocation))
                }
            }
        } else {
            requestPermissionLauncher.launch(permissionsToRequest)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImg()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null ) {
            currentImgUri = uri
            showImg()
        } else {
            Log.d("Add Data", "Tidak ada media saat ini")
        }
    }

    private fun showImg() {
        currentImgUri?.let {
            Log.d("Image Uri", "lihat gambar $it")
            binding.imageView.setImageURI(it)
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
    }}