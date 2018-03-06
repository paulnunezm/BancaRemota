package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.nunez.bancaremota.framework.respository.data.Game
import io.reactivex.Single


interface TicketBriefContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showAvailablePlays(plays: List<Game>)
        fun showUnavailablePlaysDialog(plays: List<Game>)
        fun showUnexpectedError()
        fun showConnectivityError()
        fun printReceipt(ticketInfo: TicketInfo, availablePlays: ArrayList<Game>)
    }

    interface Presenter {
        fun getPlaysAvailability(plays: List<Game>)
        fun onPrintReceipt()
    }


    interface Interactor {
        fun checkPlaysAvailability(plays: List<Game>): Single<PlayAvailabilityResponse>
        fun printTicket(id: String): Single<PrintResponse>
    }
}