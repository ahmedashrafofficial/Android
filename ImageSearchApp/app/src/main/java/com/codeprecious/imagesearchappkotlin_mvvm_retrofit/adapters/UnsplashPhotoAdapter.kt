package com.codeprecious.imagesearchappkotlin_mvvm_retrofit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.R
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.databinding.ItemUnsplashPhotoBinding
import com.codeprecious.imagesearchappkotlin_mvvm_retrofit.model.UnsplashPhoto

class UnsplashPhotoAdapter(private val listener: onItemClickListener) :
    PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.ViewHolder>(
        PHOTO_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { currentItem ->
            with(holder) {
                bind(photo = currentItem)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
//                    val item = getItem(position)
//                    if (item != null) {
//                        listener.onItemClick(item)
//                    }
                    getItem(position)?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(photo: UnsplashPhoto) {
            binding.apply {
                Glide.with(itemView).load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                tvUsername.text = photo.user.name
            }
        }
    }

    interface onItemClickListener {
        fun onItemClick(photo: UnsplashPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }

}