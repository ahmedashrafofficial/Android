package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.R
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.adapters.UnsplashPhotoAdapter
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.adapters.UnsplashPhotoLoadStateAdapter
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.databinding.FragmentGalleryBinding
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model.UnsplashPhoto
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.onItemClickListener {

    private val viewModel by viewModels<ViewModel>()
    private var _binding: FragmentGalleryBinding? =
        null    // view of fragment could be destroyed & fragment instance still exists, so we make it null on destroy to not get memory leak
    private val binding get() = _binding!! // get not nullable _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter(this)
        binding.apply {
            rv.setHasFixedSize(true)
            rv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )
            btnRetry.setOnClickListener {
                adapter.retry()
            }
        }
//        viewModel.photos.observe(viewLifecycleOwner) {
//            adapter.submitData(viewLifecycleOwner.lifecycle, it)
//        }
        viewModel.getPhotos("kitten")

        viewModel.g().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rv.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvInternet.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount == 0
                ) {
                    rv.isVisible = false
                    tvNoResults.isVisible = true
                } else {
                    tvNoResults.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true) // activate menu on fragment
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // same as if query != null
                query?.let {
                    binding.rv.scrollToPosition(0)
//                    viewModel.searchPhoto(query)
                    viewModel.getPhotos(query)
                    searchView.clearFocus() // close keyboard
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}