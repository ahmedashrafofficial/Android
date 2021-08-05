package com.codeprecious.fakedownloadapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeprecious.fakedownloadapp.R
import com.codeprecious.fakedownloadapp.databinding.ActivityMainBinding
import com.codeprecious.fakedownloadapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.apply {
            viewModel.getData().observe(this@MainActivity) {
                Log.d("asd", "onCreate: $it")
            }
        }

    }
}