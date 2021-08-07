package com.codeprecious.fakedownloadapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.codeprecious.fakedownloadapp.model.Data
import com.codeprecious.fakedownloadapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getData(): LiveData<List<Data>> {
        return repository.getData()
    }
}