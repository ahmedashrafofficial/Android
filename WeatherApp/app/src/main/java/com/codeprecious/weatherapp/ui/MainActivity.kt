package com.codeprecious.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.codeprecious.weatherapp.R
import com.codeprecious.weatherapp.adapters.RvWeatherAdapter
import com.codeprecious.weatherapp.databinding.ActivityMainBinding
import com.codeprecious.weatherapp.databinding.ActivityMainBinding.inflate
import com.codeprecious.weatherapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = RvWeatherAdapter(viewModel)

        binding.apply {
            rvWeather.adapter = adapter
            rvWeather.setHasFixedSize(true)
            progressBar.isVisible = true
        }
        viewModel.getLocationKey(viewModel.getCity())

        lifecycleScope.launchWhenStarted {
            viewModel.weatherStateFlow.collect {
                when (it) {
                    is MainViewModel.StateEvent.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                            tvCity.isVisible = true
                            tvCity.text = viewModel.getCity().toUpperCase(Locale.ROOT)
                            tvText.isVisible = true
                            tvText.text = it.data.Headline.Text!!
                        }
                        adapter.setDays(it.data.DailyForecasts)
                    }
                    is MainViewModel.StateEvent.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is MainViewModel.StateEvent.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.getLocationKey(it)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        val locationItem = menu.findItem(R.id.app_bar_location)
        locationItem.setOnMenuItemClickListener {
            binding.progressBar.isVisible = true

            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val perm = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this, perm, 1)
            } else {
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null)
            }

            return@setOnMenuItemClickListener true
        }

        val switchItem = menu.findItem(R.id.app_bar_switch)
        val switchView = switchItem?.actionView as SwitchCompat

        switchView.isChecked = viewModel.getSwitchBar()

        switchView.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setSwitchBar(isChecked)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER,
                        this,
                        null
                    )
                } catch (e: SecurityException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.progressBar.isVisible = false
                Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        viewModel.getLocationKeyByLatLong("${latitude},${longitude}")
    }
}