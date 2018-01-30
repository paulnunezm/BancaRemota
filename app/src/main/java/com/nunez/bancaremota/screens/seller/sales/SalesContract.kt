package com.nunez.bancaremota.screens.seller.sales

import io.reactivex.Observable

interface SalesContract {
    interface View {
        fun addPlay(games: Game)
        fun observeGameEntry(): Observable<Game>
        fun observeSelectedLotteries(): Observable<Lottery>
        fun showLotteriesSelector()
        fun showUserBlockedError()
        fun showNoConnectionError()
        fun showUnexpectedError()
        fun showLoading()
    }

    interface Presenter {
        fun observeGameEntry()
        fun observeLotteriesEntry()
        fun onSellButtonPressed()
    }

    interface Interactor{
        fun postPlays(plays: List<Game>)
    }
}