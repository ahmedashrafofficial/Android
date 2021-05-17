package com.codeprecious.weatherapp.model

data class Weather (
    val Headline: HeadLines,
    val DailyForecasts: List<DailyForecast>
) {
    data class HeadLines(
        val Text: String?
    )

    data class DailyForecast(
        val Date: String,
        val Temperature: Temp,
        val Day: Icon,
        val Night: Icon
    )

    data class Temp(
        val Minimum: Degree,
        val Maximum: Degree
    )

    data class Degree(
        val Value: Double,
        val Unit: String
    )

    data class Icon(
        val Icon: Int,
        val IconPhrase: String
    )
}