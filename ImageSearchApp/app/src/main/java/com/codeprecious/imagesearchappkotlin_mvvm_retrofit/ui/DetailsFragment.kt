package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.R
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val photo = args.photo

            Glide.with(this@DetailsFragment)
                .load(photo.urls.regular)
                .into(imgDetails)

            progressBar.isVisible = false
            tvCreator.isVisible = true
            tvDescription.isVisible = photo.description != null

            tvDescription.text = photo.description

            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)


            tvCreator.apply {
                text = "Photo by ${photo.user.name}"
                setOnClickListener {
                    startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }
}
