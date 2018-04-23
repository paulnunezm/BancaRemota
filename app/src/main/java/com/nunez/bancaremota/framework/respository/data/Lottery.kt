package com.nunez.bancaremota.framework.respository.data

data class LotteryRaw(
        val id: Int,
        val name: String)

class LotteryResponse(
        val success: Boolean,
        val lotteries: List<LotteryRaw>)