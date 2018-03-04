package com.nunez.bancaremota.screens.seller.tickets

import com.nunez.bancaremota.framework.respository.data.Ticket
import com.squareup.moshi.Json

class TicketsResponse(
        val success: Boolean,
        val tickets: List<Ticket>,
        @Json(name = "user_status") val userStatus: String
)