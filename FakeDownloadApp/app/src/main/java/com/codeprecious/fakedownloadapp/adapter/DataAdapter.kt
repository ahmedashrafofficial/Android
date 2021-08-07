package com.codeprecious.fakedownloadapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeprecious.fakedownloadapp.databinding.AdapterDataBinding
import com.codeprecious.fakedownloadapp.model.Data
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import javax.inject.Inject


class DataAdapter @Inject constructor(
) : ListAdapter<Data, DataAdapter.ViewHolder>(COMPARATOR()) {

    private lateinit var onClickListener: OnClickListener
    private var lastChecked = -1
    private var downloading: MutableList<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun setListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(private val binding: AdapterDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnDownload.setOnClickListener {
                if (binding.btnDownload.isEnabled) {
                    binding.btnDownload.isEnabled = false
                    download(getItem(adapterPosition).url, getItem(adapterPosition).name)
                    itemView.setBackgroundColor(Color.CYAN)
                    downloading.add(adapterPosition)
                }
            }
            itemView.setOnClickListener {
                if (binding.btnDownload.isEnabled) {
                    if (lastChecked != adapterPosition) {
                        itemView.setBackgroundColor(Color.YELLOW)
                        notifyItemChanged(lastChecked)
                        lastChecked = adapterPosition
                    }
                }
            }
        }

        fun bind() {
            binding.apply {
                tvName.text = getItem(adapterPosition).name
                tvType.text = getItem(adapterPosition).type
            }

            if (lastChecked != adapterPosition) {
                if (downloading.contains(adapterPosition)) {
                    itemView.setBackgroundColor(Color.GREEN)
                    binding.btnDownload.isEnabled = false
                } else {
                    itemView.setBackgroundColor(Color.WHITE)
                    binding.btnDownload.isEnabled = true
                }
            }
        }

        private fun download(url: String, fileName: String) {
            PRDownloader.download(url, itemView.context.filesDir.absolutePath, fileName)
                .build()
                .setOnProgressListener {
                    binding.apply {
                        progressBarDownload.max = it.totalBytes.toInt()
                        progressBarDownload.progress = it.currentBytes.toInt()
                        progressBarDownload.isVisible = true
                        tvPercentage.isVisible = true
                        tvPercentage.text = "${(it.currentBytes * 100 / it.totalBytes)}%"
                    }
                }.start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        binding.apply {
                            progressBarDownload.max = 100
                            progressBarDownload.progress = 100
                            progressBarDownload.isVisible = false
                            tvPercentage.text = "100%"
                            tvPercentage.isVisible = false
                            itemView.setBackgroundColor(Color.GREEN)

                            Toast.makeText(
                                itemView.context,
                                "Download complete",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Log.d(
                                "asd",
                                "onDownloadComplete: ${itemView.context.filesDir.absolutePath}"
                            )
                        }
                    }

                    override fun onError(error: Error?) {
                        Toast.makeText(
                            itemView.context,
                            "Failed to download this url",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }

    }

    interface OnClickListener {
        fun onClick(data: Data)
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