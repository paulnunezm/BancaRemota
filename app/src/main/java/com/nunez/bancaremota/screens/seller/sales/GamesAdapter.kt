package com.nunez.bancaremota.screens.seller.sales;

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nunez.bancaremota.R
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
            Log.v("Holder", "binding ->${game.lottery_name}")
            lateinit var gameNumbers: String
            lateinit var gameTypeText: String

            when (game.type_id) {
                Game.TYPE_QUINIELA -> {
                    gameTypeText = Quiniela::class.java.simpleName
                    gameNumbers = game.first.toString()
                }
                Game.TYPE_PALE -> {
                    gameTypeText = Pale::class.java.simpleName
                    gameNumbers = "${game.first}-${game.second}"
                }
                Game.TYPE_TRIPLETA -> {
                    gameTypeText = Tripleta::class.java.simpleName
                    gameNumbers = "${game.first}-${game.second}-${game.third}"
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
            amount.text = game.amount.toString()
        }
    }
}