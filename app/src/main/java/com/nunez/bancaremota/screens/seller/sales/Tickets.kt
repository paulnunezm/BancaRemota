package com.nunez.bancaremota.screens.seller.sales

open class Game(
        open val first: Int,
        open val second: Int?,
        open val third: Int?,
        open val amount: Int,
        open val lottery_id: Int,
        open val type_id: Int
) {
    companion object {
        const val TYPE_QUINIELA = 3
        const val TYPE_PALE = 1
        const val TYPE_TRIPLETA = 2
        const val LOTTERY_NOT_ASSIGNED = 0
    }
}

data class Quiniela(
        override val first: Int,
        override val amount: Int,
        override val lottery_id: Int
) : Game(first, null, null, amount, lottery_id, Game.TYPE_QUINIELA)

data class Pale(
        override val first: Int,
        override val second: Int,
        override val amount: Int,
        override val lottery_id: Int
) : Game(first, second, null, amount, lottery_id, Game.TYPE_PALE)

data class Tripleta(
        override val first: Int,
        override val second: Int,
        override val third: Int,
        override val amount: Int,
        override val lottery_id: Int
) : Game(first, second, third, amount, lottery_id, Game.TYPE_TRIPLETA)

data class Ticket(
        val number: String,
        val blocked: Boolean,
        val printed: Boolean,
        val games: List<Game>)