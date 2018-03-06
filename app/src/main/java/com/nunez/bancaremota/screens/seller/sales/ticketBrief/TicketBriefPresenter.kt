package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.palcine.framework.exceptions.NoConnectionException

class TicketBriefPresenter(
        val view: TicketBriefContract.View,
        val interactor: TicketBriefContract.Interactor
) : TicketBriefContract.Presenter {

    private lateinit var availablePlays: ArrayList<Game>
    private lateinit var ticketInfo: TicketInfo

    override fun getPlaysAvailability(plays: List<Game>) {
        view.showLoading()

        interactor.checkPlaysAvailability(plays)
                .subscribe({ response: PlayAvailabilityResponse ->
                    if (response.success) {
                        view.hideLoading()
                        ticketInfo = response.ticketInfo
                        filterPlaysAndShowCorrectOnes(plays, response)
                    } else {
                        view.hideLoading()
                        view.showUnexpectedError()
                    }
                }, {
                    view.hideLoading()
                    if (it is NoConnectionException) {
                        view.showConnectivityError()
                    } else {
                        view.showUnexpectedError()
                    }
                })
    }

    override fun onPrintReceipt() {
        view.showLoading()
        interactor.printTicket(ticketInfo.id)
                .subscribe({
                    view.hideLoading()
                    if (it.success) {
                        view.printReceipt(ticketInfo, availablePlays)
                    } else {
                        view.showUnexpectedError()
                    }
                }, {
                    view.hideLoading()
                    if (it is NoConnectionException) {
                        view.showConnectivityError()
                    } else {
                        view.showUnexpectedError()
                    }
                })
    }

    private fun filterPlaysAndShowCorrectOnes(plays: List<Game>, response: PlayAvailabilityResponse) {
        var unavailablePlays = ArrayList<Game>()
        availablePlays = ArrayList()

        plays.mapIndexed { index, game ->
            if (response.game[index].code == 0) {
                availablePlays.add(game)
            } else {
                unavailablePlays.add(game)
            }
        }

        showUnavailablePlaysIfTheyAre(unavailablePlays)
        view.showAvailablePlays(availablePlays)
    }

    private fun showUnavailablePlaysIfTheyAre(list: ArrayList<Game>) {
        if (list.size > 1) {
            view.showUnavailablePlaysDialog(list)
        }
    }
}