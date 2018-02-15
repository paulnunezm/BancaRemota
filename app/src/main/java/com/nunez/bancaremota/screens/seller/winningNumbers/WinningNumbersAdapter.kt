package com.nunez.bancaremota.screens.seller.winningNumbers;

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nunez.bancaremota.R

class WinningNumbersAdapter(
        private val context: Context,
        private val items: List<WinningNumbers>
) : RecyclerView.Adapter<WinningNumbersAdapter.WinningNumberHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WinningNumberHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_winning_numbers, parent, false)
        return WinningNumberHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: WinningNumberHolder?, position: Int) {
        holder?.bind(items[position])
    }

    class WinningNumberHolder(
            itemView: View
    ): RecyclerView.ViewHolder(itemView){
        fun bind(item: WinningNumbers){
           itemView.findViewById<TextView>(R.id.lotteryName).text = item.lottery.name
           itemView.findViewById<TextView>(R.id.first).text = item.first
           itemView.findViewById<TextView>(R.id.second).text = item.second
           itemView.findViewById<TextView>(R.id.third).text = item.second
        }
    }
}