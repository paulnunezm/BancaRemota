package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.nunez.bancaremota.framework.helpers.FormatterHelper
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
                        view.showTicketNumber(FormatterHelper.getFormattedTicketNumber(ticketInfo.number))
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
        val unavailablePlays = ArrayList<Game>()
        availablePlays = ArrayList()

        plays.mapIndexed { index, game ->
            if (response.game[index].code == 0) {
                availablePlays.add(game)
            } else {
                unavailablePlays.add(game)
            }
        }

        showUnavailablePlaysIfTheyAre(unavailablePlays)
        handleAvailablePlays()
    }

    private fun handleAvailablePlays() {
        if (availablePlays.isNotEmpty()) {
            view.showTotalAmount(getFormattedTotalAmount())
            view.showAvailablePlays(availablePlays)
        } else {
            view.showNoAvailablePlaysError()
        }
    }

    private fun getFormattedTotalAmount(): String {
        return FormatterHelper.twoDigitsStringFormatter(availablePlays.sumByDouble {
            it.amount.toDouble()
        })
    }

    private fun showUnavailablePlaysIfTheyAre(list: ArrayList<Game>) {
        if (list.isNotEmpty()) {
            view.showUnavailablePlaysDialog(list)
        }
    }
}