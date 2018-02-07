package com.nunez.bancaremota.screens.seller.sales

import com.nunez.bancaremota.framework.exceptions.NotAvailableLotteriesException
import com.nunez.palcine.framework.exceptions.NoConnectionException

class SalesPresenter(
        private val view: SalesContract.View,
        private val interactor: SalesContract.Interactor
) : SalesContract.Presenter {

    private lateinit var currentPlay: Game
    internal var plays = ArrayList<Game>()

    override fun observeGameEntry() {
        view.observeGameEntry()
                .subscribe(
                        {
                            currentPlay = it
                            view.showLotteriesSelector()
                        },
                        this::handleErrors)
    }

    override fun observeLotteryEntry() {
        view.observeSelectedLotteries()
                .subscribe(
                        this::handleLotterySelected,
                        this::handleErrors)
    }

    override fun onSellButtonPressed() {
        view.showLoading()
        interactor.postPlays(plays)
    }

    override fun onPlayDeleted(position: Int) {
        plays.removeAt(position)

        if(plays.isEmpty()){
            view.hideProcessOrderButton()
        }
    }


    override fun onItemSwipe(position: Int) {
        plays.removeAt(position)
        view.removeItemFromList(position)
        view.showErasedPlayMessage()
    }

    private fun handleLotterySelected(l: Lottery) {
        var lottery = l
        lateinit var playWithLottery: Game
        with(currentPlay) {
            playWithLottery = Game(first, second, third,
                    amount, lottery.id, lottery.name, type_id)
        }
        plays.add(playWithLottery)
        view.addPlay(playWithLottery)
        if(plays.size < 2){
            view.showProcessOrderButton()
        }
    }

    private fun handleErrors(t: Throwable) {
        when (t) {
            is NotAvailableLotteriesException -> view.showUnexpectedError()
            is NoConnectionException -> view.showNoConnectionError()
            else -> view.showUnexpectedError()
        }
    }
}