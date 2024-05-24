package com.dicoding.aplikasistoryku.view.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.aplikasistoryku.R
import com.dicoding.aplikasistoryku.data.api.ApiConfig
import com.dicoding.aplikasistoryku.data.api.ApiService
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.databinding.ActivityDetailBinding
import com.dicoding.aplikasistoryku.view.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var apiService: ApiService
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val storyId = intent.getStringExtra("STORY_ID")


        // Inisialisasi ApiService dengan endpoint yang benar
        apiService = ApiConfig.getApiService()

        // Inisialisasi DetailViewModel menggunakan ViewModelProvider
        detailViewModel = ViewModelProvider(this, ViewModelFactory(applicationContext)).get(DetailViewModel::class.java)

        // Memulai coroutine untuk memanggil getToken() dari DetailViewModel
        CoroutineScope(Dispatchers.Main).launch {
            val token = detailViewModel.getToken().value
            token?.let {
                try {
                    val response = apiService.getStoryDetail(storyId!!, "Bearer $it")
                    withContext(Dispatchers.Main) {
                        if (!response.error!!) {
                            val story = response.story
                            // Menggunakan Glide untuk menampilkan gambar dari URL
                            Glide.with(this@DetailActivity)
                                .load(story?.photoUrl)
                                .into(binding.imgPhoto)
                            binding.tvName.text = story?.name
                            binding.tvDescription.text = story?.description
                        } else {
                            Log.e("API_ERROR", response.message ?: "Unknown error")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", e.message ?: "Unknown error")
                }
            }
        }
    }
}