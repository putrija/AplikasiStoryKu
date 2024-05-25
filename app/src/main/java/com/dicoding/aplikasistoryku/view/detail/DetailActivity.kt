package com.dicoding.aplikasistoryku.view.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.aplikasistoryku.data.api.ApiConfig
import com.dicoding.aplikasistoryku.data.api.ApiService
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


        apiService = ApiConfig.getApiService()

        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(applicationContext)
        ).get(DetailViewModel::class.java)

        showLoading(true)

        CoroutineScope(Dispatchers.Main).launch {
            val token = detailViewModel.getToken().value
            token?.let {
                try {
                    val response = apiService.getStoryDetail(storyId!!, "Bearer $it")
                    withContext(Dispatchers.Main) {
                        if (!response.error!!) {
                            val story = response.story
                            Glide.with(this@DetailActivity)
                                .load(story?.photoUrl)
                                .into(binding.ivDetailPhoto)
                            binding.tvDetailName.text = story?.name
                            binding.tvDetailDescription.text = story?.description
                        } else {
                            Log.e("API_ERROR", response.message ?: "Unknown error")
                        }
                        showLoading(false)
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", e.message ?: "Unknown error")
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}