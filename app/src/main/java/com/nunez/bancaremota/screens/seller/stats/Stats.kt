package com.nunez.bancaremota.screens.seller.stats
import com.squareup.moshi.Json

class Stats(
        val sales: String,
        val commission: String,
        val prices: String,
        val total: String)

class StatsRespone(
        val success: Boolean,
        @Json(name = "total_sales") val sales: String?,
        @Json(name = "total_win") val won: String?,
        @Json(name = "total_commission") val comission: String?,
        @Json(name = "total_balance") val balace: String?)
