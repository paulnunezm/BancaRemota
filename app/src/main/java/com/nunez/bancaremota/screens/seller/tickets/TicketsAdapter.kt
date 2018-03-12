package com.nunez.bancaremota.screens.seller.tickets;

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.FormatterHelper
import com.nunez.bancaremota.framework.respository.data.Ticket


class TicketsAdapter(
        private val context: Context,
        private val items: List<Ticket>,
        private val clickListener: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TicketViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TicketViewHolder?, position: Int) {
        val item = items[position]
        holder?.bind(item,clickListener)
    }

    class TicketViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind(ticket: Ticket, clickListener: (Ticket) -> Unit) {
            with(ticket) {
                itemView.findViewById<TextView>(R.id.number).text = FormatterHelper.getFormattedTicketNumber(number)
                itemView.findViewById<TextView>(R.id.amount).text = "$currency $amount"
                itemView.findViewById<TextView>(R.id.status).text = getStatusMessage(this)
            }

            itemView.setOnClickListener {
                clickListener(ticket)
            }
        }

        private fun hasWinningPlays(ticket: Ticket) = ticket.games.any { it.winner }

        private fun getStatusMessage(ticket: Ticket): String {
            return when {
                ticket.paid -> itemView.context.getString(R.string.tickets_msg_paid)
                hasWinningPlays(ticket) -> itemView.context.getString(R.string.tickets_msg_winner)
                else -> itemView.context.getString(R.string.tickets_msg_played)
            }
        }
    }
}