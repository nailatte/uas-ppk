package com.example.sipora.data.remote.response

// This is a placeholder data structure based on the UI screenshot
data class StatisticResponse(
    val ormawaName: String,
    val stats: List<StatItem>,
    val passRates: List<PassRateItem>
)

data class StatItem(
    val label: String,
    val value: Int
)

data class PassRateItem(
    val label: String,
    val value: Double
)
