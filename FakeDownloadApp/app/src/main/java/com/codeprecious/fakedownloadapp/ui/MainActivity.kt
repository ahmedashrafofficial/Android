package com.codeprecious.fakedownloadapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeprecious.fakedownloadapp.R
import com.codeprecious.fakedownloadapp.adapter.DataAdapter
import com.codeprecious.fakedownloadapp.databinding.ActivityMainBinding
import com.codeprecious.fakedownloadapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.apply {

            rvData.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = dataAdapter
            }
            
            viewModel.getData().observe(this@MainActivity) {
                dataAdapter.submitList(it)
            }
        }

    }
}