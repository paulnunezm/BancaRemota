package com.nunez.bancaremota.screens.seller.tickets.ticketDetails

import com.nunez.bancaremota.framework.respository.data.Game

interface TicketDetailsContract {

    interface View {
        fun hidePayButton()
        fun hidePrintButton()
        fun showNumberAndAmount(number: String, amount: String)
        fun showPlays(plays: List<Game>)
        fun showBlockedStatus()
        fun showPaidStatus()
        fun showPrintedStatus()
        fun showUnprintedStatus()
        fun showPaymentBottomSheet(id: String, currency: String, amountToPay: String)
        fun showWinnerStatus()
    }

    interface Presenter {
        fun showTicket()
        fun onPayClicked()
    }
}