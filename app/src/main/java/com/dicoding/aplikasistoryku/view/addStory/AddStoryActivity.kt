package com.dicoding.aplikasistoryku.view.addStory

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dicoding.aplikasistoryku.R
import com.dicoding.aplikasistoryku.data.api.ApiResponse
import com.dicoding.aplikasistoryku.databinding.ActivityAddStoryBinding
import com.dicoding.aplikasistoryku.getImageUri
import com.dicoding.aplikasistoryku.reduceFileImage
import com.dicoding.aplikasistoryku.uriToFile
import com.dicoding.aplikasistoryku.view.ViewModelFactory
import com.dicoding.aplikasistoryku.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddStoryViewModel> { ViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }

        savedInstanceState?.getString("currentImageUri")?.let { imageUriString ->
            currentImageUri = imageUriString.toUri()
            showImage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentImageUri", currentImageUri?.toString())
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")

            CoroutineScope(Dispatchers.Main).launch {
                if (imageFile.exists()) {
                    val description = binding.edAddDescription.text.toString()

                    if (description.isNotEmpty()) {
                        try {
                            val token = viewModel.getToken()
                            viewModel.uploadStory(imageFile, description, token)
                                .observe(this@AddStoryActivity) { result ->
                                    if (result != null) {
                                        when (result) {
                                            is ApiResponse.Loading -> {
                                                showLoading(true)
                                            }

                                            is ApiResponse.Success -> {
                                                showToast(result.data.message)
                                                showLoading(false)

                                                val intent = Intent(
                                                    this@AddStoryActivity,
                                                    MainActivity::class.java
                                                )
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(intent)
                                            }

                                            is ApiResponse.Error -> {
                                                showToast(result.errorMessage)
                                                showLoading(false)
                                            }
                                        }
                                    }
                                }
                        } catch (e: Exception) {
                            Log.e("Error", "Error getting token: ${e.message}")
                        }
                    } else {
                        showToast(getString(R.string.empty_description_warning))
                    }
                } else {
                    showToast(getString(R.string.empty_image_warning))
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}