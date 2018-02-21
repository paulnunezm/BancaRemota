package com.nunez.bancaremota.framework.respository.data

open class Game(
        open val first: Int,
        open val second: Int?,
        open val third: Int?,
        open val amount: Int,
        open var lottery_id: Int,
        open var lottery_name: String,
        open val type_id: Int
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
        override val amount: Int
) : Game(first, null, null, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_QUINIELA)

data class Pale(
        override val first: Int,
        override val second: Int,
        override val amount: Int
) : Game(first, second, null, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_PALE)

data class Tripleta(
        override val first: Int,
        override val second: Int,
        override val third: Int,
        override val amount: Int
) : Game(first, second, third, amount, LOTTERY_NOT_ASSIGNED, "", TYPE_TRIPLETA)

data class Ticket(
        val number: String,
        val blocked: Boolean,
        val printed: Boolean,
        val games: List<Game>)