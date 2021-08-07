package com.codeprecious.fakedownloadapp.adapter

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Environment
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject


class DataAdapter @Inject constructor(
) : ListAdapter<Data, DataAdapter.ViewHolder>(COMPARATOR()) {

    private var lastChecked = -1
    private var downloadingFiles: MutableList<Int> = ArrayList()
    private val disposables = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AdapterDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(private val binding: AdapterDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var downloading = true

        init {
            binding.btnDownload.setOnClickListener {
                if (binding.btnDownload.isEnabled) {
                    binding.btnDownload.isEnabled = false
                    downloadFiles(getItem(adapterPosition).url, getItem(adapterPosition).name)
                    itemView.setBackgroundColor(Color.CYAN)
                    downloadingFiles.add(adapterPosition)
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
                if (downloadingFiles.contains(adapterPosition)) {
                    itemView.setBackgroundColor(Color.GREEN)
                    binding.btnDownload.isEnabled = false
                } else {
                    itemView.setBackgroundColor(Color.WHITE)
                    binding.btnDownload.isEnabled = true
                }
            }
        }

        private fun download(url: String, fileName: String) {
            PRDownloader.download(
                url,
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
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

        private fun downloadFiles(uri: String, fileName: String) {
            val request = DownloadManager.Request(Uri.parse(uri))
            request.setTitle(fileName)
            request.setDescription("Downloading file, Please wait")
            val cookie = android.webkit.CookieManager.getInstance().getCookie(uri)
            request.addRequestHeader("cookie", cookie)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            val manager: DownloadManager =
                itemView.context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val downloadId = manager.enqueue(request)

            Toast.makeText(itemView.context, "Download Started", Toast.LENGTH_LONG).show()

            io.reactivex.rxjava3.core.Observable.fromCallable {
                downloading(downloadId, manager)
            }.subscribeOn(Schedulers.io())
                .repeatUntil {
                    !downloading
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onSubscribe(d: Disposable?) {
                        disposables.add(d)
                    }

                    override fun onNext(percentage: Int?) {
                        Log.d("asd", "onNext: $percentage")
                        binding.apply {
                            progressBarDownload.isVisible = true
                            tvPercentage.isVisible = true
                            tvPercentage.text = "${percentage}%"
                        }
                    }

                    override fun onError(e: Throwable?) {
                        Toast.makeText(
                            itemView.context,
                            e?.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                    override fun onComplete() {
                        binding.apply {
                            progressBarDownload.max = 100
                            progressBarDownload.progress = 100
                            tvPercentage.text = "100%"
                            itemView.setBackgroundColor(Color.GREEN)

                            Toast.makeText(
                                itemView.context,
                                "Download complete",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }
                })
        }

        private fun downloading(downloadId: Long, manager: DownloadManager): Int {
            val q = DownloadManager.Query()
            q.setFilterById(downloadId)

            val cursor: Cursor = manager.query(q)
            cursor.moveToFirst()
            val bytes_downloaded = cursor.getInt(
                cursor
                    .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
            )
            val bytes_total =
                cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            binding.apply {
                progressBarDownload.max = bytes_total
                progressBarDownload.progress = bytes_downloaded
            }

            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                downloading = false
            }

            cursor.close()

            return (bytes_downloaded * 100L / bytes_total).toInt()
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