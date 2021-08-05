package com.codeprecious.fakedownloadapp.viewModel

import androidx.lifecycle.ViewModel
import com.codeprecious.fakedownloadapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class viewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    
}