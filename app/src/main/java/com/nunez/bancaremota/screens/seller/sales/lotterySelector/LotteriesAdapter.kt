package com.nunez.bancaremota.screens.seller.sales.lotterySelector;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.nunez.bancaremota.R
import com.nunez.bancaremota.screens.seller.sales.Lottery

class LotteriesAdapter(
        var items: List<Lottery>
) : RecyclerView.Adapter<LotteriesAdapter.LotteryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LotteryViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.lottery_selector_item, parent, false)
        return LotteryViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LotteryViewHolder?, position: Int) {
        val item = items[position]
        holder?.bind(item, {
            item.selected = it
        })
    }

    class LotteryViewHolder(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(lottery: Lottery, clickListener: (Boolean) -> Unit) {

            val checkbox = itemView.findViewById<CheckBox>(R.id.checkbox)

            checkbox.text = lottery.name
            checkbox.setOnClickListener {
                clickListener(checkbox.isChecked)
            }
        }
    }
}