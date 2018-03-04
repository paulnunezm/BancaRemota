package com.nunez.bancaremota.framework.respository.data

import com.squareup.moshi.Json

open class Game(
        open val first: Int,
        open val second: Int?,
        open val third: Int?,
        open val amount: Float,
        open var lottery_id: Int,
        open var lottery_name: String,
        open val type_id: Int,
        open val winner: Boolean = false,
        open val amount_to_pay: Float = 0f
) {
    companion object {
        const val TYPE_QUINIELA = 3
        const val TYPE_PALE = 1
        const val TYPE_TRIPLETA = 2
        const val LOTTERY_NOT_ASSIGNED = 0
    }
}

data class Lottery(
        val id: Int,
        val name: String,
        var selected: Boolean = false)

data class Quiniela(
        override val first: Int,
        override val amount: Float
) : Game(first, null, null, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_QUINIELA)

data class Pale(
        override val first: Int,
        override val second: Int,
        override val amount: Float
) : Game(first, second, null, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_PALE)

data class Tripleta(
        override val first: Int,
        override val second: Int,
        override val third: Int,
        override val amount: Float
) : Game(first, second, third, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_TRIPLETA)

data class Ticket(
        @Json(name = "ticket_id") val id: String,
        @Json(name = "ticket_number") val number: String,
        @Json(name = "ticket_amount") val amount: Float,
        @Json(name = "ticket_currency") val currency: String,
        @Json(name = "ticket_blocked") val blocked: Boolean,
        @Json(name = "ticket_paid") val paid: Boolean,
        @Json(name = "ticket_printed") val printed: Boolean,
        val games: List<Game>)