package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.squareup.moshi.Json

class PlayAvailabilityResponse(
        val success: Boolean,
        @Json(name = "ticket_number") val ticketInfo: TicketInfo,
        @Json(name = "game_status") val game: List<PlayAvailability>)

class PlayAvailability(
        val index: Int,
        val code: Int) {

    companion object {
        const val CODE_NO_ERROR = 0
        const val CODE_SCHEDULE_ERROR = 1
        const val CODE_AMOUNT_EXCEEDED = 2
        const val CODE_LIMIT_EXCEEDED = 3
    }
}

class TicketInfo(
        val id:String,
        val number: String?,
        @Json(name = "created_at") val createdAt: String)
