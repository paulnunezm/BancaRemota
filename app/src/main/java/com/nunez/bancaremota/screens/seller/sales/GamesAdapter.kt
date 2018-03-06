package com.nunez.bancaremota.screens.seller.sales;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.FormatterHelper
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.framework.respository.data.Pale
import com.nunez.bancaremota.framework.respository.data.Quiniela
import com.nunez.bancaremota.framework.respository.data.Tripleta


class GamesAdapter(
        private val items: ArrayList<Game>
) : RecyclerView.Adapter<GamesAdapter.GamesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GamesHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_sales_game, parent, false)
        return GamesHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GamesHolder?, position: Int) {
        val item = items[position]
        holder?.bind(item)
    }

    fun onItemDismissed(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class GamesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(game: Game) {
            lateinit var gameNumbers: String
            lateinit var gameTypeText: String

            when (game.type_id) {
                Game.TYPE_QUINIELA -> {
                    gameTypeText = Quiniela::class.java.simpleName
                    gameNumbers =FormatterHelper.twoDigitsStringFormatter(game.first)
                }
                Game.TYPE_PALE -> {
                    gameTypeText = Pale::class.java.simpleName
                    val f = FormatterHelper.twoDigitsStringFormatter(game.first)
                    val s = FormatterHelper.twoDigitsStringFormatter(game.second as Number)
                    gameNumbers = "$f - $s"
                }
                Game.TYPE_TRIPLETA -> {
                    gameTypeText = Tripleta::class.java.simpleName
                    val f = FormatterHelper.twoDigitsStringFormatter(game.first)
                    val s = FormatterHelper.twoDigitsStringFormatter(game.second as Number)
                    val t = FormatterHelper.twoDigitsStringFormatter(game.third as Number)
                    gameNumbers = "$f - $s - $t"

                }
                else -> {
                    gameTypeText = ""
                    gameNumbers= ""
                }
            }

            val lotteryName = itemView.findViewById<TextView>(R.id.lotteryName)
            val gameType = itemView.findViewById<TextView>(R.id.gameType)
            val numbers = itemView.findViewById<TextView>(R.id.numbers)
            val amount = itemView.findViewById<TextView>(R.id.amount)

            lotteryName.text = game.lottery_name
            gameType.text = gameTypeText
            numbers.text = gameNumbers
            amount.text = itemView.context.getString(R.string.sale_screen_amount, game.amount.toString())
        }
    }
}