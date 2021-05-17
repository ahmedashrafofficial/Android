package com.codeprecious.weatherapp.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeprecious.weatherapp.R
import com.codeprecious.weatherapp.util.Util
import com.codeprecious.weatherapp.databinding.RvItemBinding
import com.codeprecious.weatherapp.model.Weather
import com.codeprecious.weatherapp.viewmodel.MainViewModel
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class RvWeatherAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<RvWeatherAdapter.ViewHolder>() {

    private var days: List<Weather.DailyForecast> = ArrayList()

    fun setDays(days: List<Weather.DailyForecast>): List<Weather.DailyForecast> {
        return days.also {
            this.days = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int {
        return days.size
    }

    inner class ViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Weather.DailyForecast) {
            binding.apply {

                val dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.parse("${day.Date.split(":")[0]}:00:00")
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                tvDay.text = dateTime.dayOfWeek.toString()


                var icon = Util.getIcon(day.Day.Icon)

                Glide.with(itemView).load(icon)
                    .error(R.drawable.ic_error)
                    .into(imgDay)

                if (viewModel.getSwitchBar()) {
                    tvDayTemp.text = "H/${day.Temperature.Maximum.Value}C"
                } else {
                    tvDayTemp.text = "H/${day.Temperature.Maximum.Value}F"
                }

                icon = Util.getIcon(day.Night.Icon)
                Glide.with(itemView).load(icon)
                    .error(R.drawable.ic_error)
                    .into(imgNight)
                if (viewModel.getSwitchBar()) {
                    tvNightTemp.text = "L/${day.Temperature.Minimum.Value}C "
                } else {
                    tvNightTemp.text = "L/${day.Temperature.Minimum.Value}F"
                }

            }
        }
    }
}