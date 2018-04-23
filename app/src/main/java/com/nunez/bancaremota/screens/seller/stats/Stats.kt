package com.nunez.bancaremota.screens.seller.stats
import com.squareup.moshi.Json

class Stats(
        val sales: String,
        val commission: String,
        val won: String,
        val total: String)

class StatsResponse(
        val success: Boolean,
        @Json(name = "total_sales") val sales: String?,
        @Json(name = "total_win") val won: String?,
        @Json(name = "total_commission") val commission: String?,
        @Json(name = "total_balance") val balance: String?)
