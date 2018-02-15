package com.nunez.bancaremota.screens.seller.winningNumbers

data class WinningNumbersResponse(
        val success: Boolean,
        val numbers: List<WinningNumbers>)

data class WinningNumbers(
        val first: String,
        val second: String,
        val third: String,
        val shift: String,
        val lottery: Lottery)

data class Lottery(
        val id: Int,
        val name: String)
