package com.nunez.bancaremota.screens.seller.sales

import io.reactivex.Observable

interface SalesContract {
    interface View {
        fun addPlay(games: Game)
        fun goToTicketBriefFragment(plays: List<Game>)
        fun hideProcessOrderButton()
        fun observeGameEntry(): Observable<Game>
        fun observeSelectedLotteries(): Observable<Lottery>
        fun removeItemFromList(position: Int)
        fun showLotteriesSelector()
        fun showUserBlockedError()
        fun showNoConnectionError()
        fun showUnexpectedError()
        fun showLoading()
        fun showProcessOrderButton()
        fun showErasedPlayMessage()
    }

    interface Presenter {
        fun observeGameEntry()
        fun observeLotteryEntry()
        fun onItemSwipe(position: Int)
        fun onPlayDeleted(position: Int)
        fun onSellButtonPressed()
    }
}