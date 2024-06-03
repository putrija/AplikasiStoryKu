package com.dicoding.aplikasistoryku.view.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.dicoding.aplikasistoryku.R
import com.dicoding.aplikasistoryku.data.repository.UserRepository
import com.dicoding.aplikasistoryku.data.response.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.aplikasistoryku.databinding.ActivityMapsBinding
import com.dicoding.aplikasistoryku.view.ViewModelFactory
import com.dicoding.aplikasistoryku.view.main.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> { ViewModelFactory(this) }
    private lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        showAllStory()
    }


    private fun addMarkersToMap(storyList: List<ListStoryItem>) {
        storyList.forEach { story ->
            val latLng = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            val markerOptions = MarkerOptions().position(latLng).title(story.name).snippet(story.description)
            mMap.addMarker(markerOptions)
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    private fun showAllStory() {
        viewModel.storyResponse.observe(this, Observer { storyResponse ->
            if (storyResponse != null && !storyResponse.listStory.isNullOrEmpty()) {
                Log.d("MapsActivity", "Total Stories: ${storyResponse.listStory.size}")
                addMarkersToMap(storyResponse.listStory)
            } else {
                Log.d("MapsActivity", "No Stories Available")
            }
        })

        viewModel.fetchStories()
    }




//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                getMyLocation()
//            }
//        }
//
//    private fun getMyLocation() {
//        if (ContextCompat.checkSelfPermission(
//                this.applicationContext,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            mMap.isMyLocationEnabled = true
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//    }

//    private fun setMapStyle() {
//        try {
//            val success =
//                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.")
//            }
//        } catch (exception: Resources.NotFoundException) {
//            Log.e(TAG, "Can't find style. Error: ", exception)
//        }
//    }

//    companion object {
//        private const val TAG = "MapsActivity"
//        // https://latitudelongitude.org/id/
//        // seems above link is not accurate, so I use this instead
//        // https://en.wikipedia.org/wiki/Module:Location_map/data/Southeast_Asia
//        private val INDONESIA_LATITUDE_RANGE = -12.0 to 12.0
//        private val INDONESIA_LONGITUDE_RANGE = 90.0 to 150.0
//    }

    private fun showToast(message: String) {
        Toast.makeText(this@MapsActivity, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

}