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
        fun showProcessOrderButton()
        fun hideProcessOrderButton()
        fun removeItemFromList(position: Int)
        fun showErasedPlayMessage()
    }

    interface Presenter {
        fun observeGameEntry()
        fun observeLotteryEntry()
        fun onSellButtonPressed()
        fun onPlayDeleted(position: Int)
        fun onItemSwipe(position: Int)
    }

    interface Interactor{
        fun postPlays(plays: List<Game>)
    }
}