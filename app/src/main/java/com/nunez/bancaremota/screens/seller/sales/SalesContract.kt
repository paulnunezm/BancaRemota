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
        fun showErasedPlayMessage()
        fun showLoading()
        fun showLotteriesSelector()
        fun showNoAvailableLotteriesError()
        fun showNoConnectionError()
        fun showProcessOrderButton()
        fun showUnexpectedError()
        fun showUserBlockedError()
    }

    interface Presenter {
        fun observeGameEntry()
        fun observeLotteryEntry()
        fun onItemSwipe(position: Int)
        fun onPlayDeleted(position: Int)
        fun onSellButtonPressed()
    }
}