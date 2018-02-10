package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.nunez.bancaremota.screens.seller.sales.Game
import com.nunez.palcine.framework.exceptions.NoConnectionException

class TicketBriefPresenter(
        val view: TicketBriefContract.View,
        val interactor: TicketBriefContract.Interactor
) : TicketBriefContract.Presenter {

    override fun getPlaysAvailability(plays: List<Game>) {
        view.showLoading()

        interactor.checkPlaysAvailability(plays)
                .subscribe({ response: PlayAvailabilityResponse ->
                    if (response.success) {
                        view.hideLoading()
                        filterPlaysAndShowCorrectOnes(plays, response)
                    } else {
                        view.hideLoading()
                        view.showUnexpectedError()
                    }
                }, {
                    view.hideLoading()
                    if (it is NoConnectionException) {
                        view.showConnectivityError()
                    }else{
                        view.showUnexpectedError()
                    }
                })
    }

    override fun onPrintReceipt() {
    }

    private fun filterPlaysAndShowCorrectOnes(plays: List<Game>, response: PlayAvailabilityResponse) {
        var unavailablePlays = ArrayList<Game>()
        var availablePlays = ArrayList<Game>()

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