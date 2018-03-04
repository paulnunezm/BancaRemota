package com.nunez.bancaremota.screens.seller.tickets.ticketDetails

import com.nunez.bancaremota.framework.respository.data.Ticket

class TicketDetailsPresenter(
        val view: TicketDetailsContract.View,
        val ticket: Ticket
) : TicketDetailsContract.Presenter {

    override fun onPayClicked() {
        val amount = ticket.games
                .filter { it.winner }
                .sumByDouble { it.amount_to_pay.toDouble() }
                .toString()
        view.showPaymentBottomSheet(ticket.id, ticket.currency, amount)
    }

    override fun showTicket() {
        handleButtonVisibility()
        setStatus()
        view.showNumberAndAmount(ticket.number, "${ticket.currency} ${ticket.amount}")
        view.showPlays(ticket.games)
    }

    private fun setStatus() {
        when {
            ticketWithWinningPlays() -> view.showWinnerStatus()
            ticket.blocked -> view.showBlockedStatus()
            ticket.paid -> view.showPaidStatus()
            ticket.printed -> view.showPrintedStatus()
            else -> view.showUnprintedStatus()
        }
    }

    private fun handleButtonVisibility() {
        if (ticket.blocked) {
            view.hidePayButton()
            view.hidePrintButton()
        } else {
            if (ticket.paid || ticketWithoutWinningPlays()) {
                view.hidePayButton()
            }

            if (ticket.printed) {
                view.hidePrintButton()
            }
        }
    }

    private fun ticketWithoutWinningPlays(): Boolean {
        return ticket.games.none({ it.winner })
    }

    private fun ticketWithWinningPlays(): Boolean {
        return ticket.games.any({ it.winner })
    }
}