package com.codeprecious.fakedownloadapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeprecious.fakedownloadapp.adapter.DataAdapter
import com.codeprecious.fakedownloadapp.databinding.ActivityMainBinding
import com.codeprecious.fakedownloadapp.model.Data
import com.codeprecious.fakedownloadapp.viewModel.MainViewModel
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var dataAdapter: DataAdapter

    @Inject
    lateinit var config: PRDownloaderConfig

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        PRDownloader.initialize(baseContext, config)


        binding.apply {
            rvData.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = dataAdapter
            }

            viewModel.getData()?.observe(this@MainActivity) {
                if (it != null) {
                    dataAdapter.submitList(it)
                }
            }

            dataAdapter.setListener(object : DataAdapter.OnClickListener {
                override fun onClick(data: Data) {
                }
            })
        }

//        hasWriteStoragePermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasWriteStoragePermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )

            return false
        }


        return true
    }
}