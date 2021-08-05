package com.codeprecious.fakedownloadapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeprecious.fakedownloadapp.databinding.AdapterDataBinding
import com.codeprecious.fakedownloadapp.model.Data
import javax.inject.Inject

class DataAdapter @Inject constructor() : ListAdapter<Data, DataAdapter.ViewHolder>(COMPARATOR()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: AdapterDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                tvName.text = getItem(adapterPosition).name
                tvType.text = getItem(adapterPosition).type
            }
        }

    }


    class COMPARATOR : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }


}