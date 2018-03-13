package com.nunez.bancaremota.screens.seller.tickets

import com.nunez.bancaremota.framework.respository.data.Ticket
import io.reactivex.subjects.PublishSubject

interface TicketsContract {
    interface View {
        fun goToTicketDetails(ticket: Ticket)
        fun showTickets(tickets: List<Ticket>)
        fun showNoConnectionError()
        fun showUserBlockedError()
        fun showUnexpectedError()
        fun showNoTicketsMessage()
        fun showLoading()
        fun hideLoading()
        fun hideTickets()
        fun hideMessage()
    }

    interface Presenter {
        fun getAllTickets()
        fun ticketClicked(ticket: Ticket)
        fun  subscribeToSearchQueryChanges(subject: PublishSubject<String>)
        fun getTicketsWithId(id: String)
    }
}