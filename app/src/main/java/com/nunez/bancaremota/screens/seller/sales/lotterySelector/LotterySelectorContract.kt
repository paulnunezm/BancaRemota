package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import com.nunez.bancaremota.screens.seller.sales.Lottery
import io.reactivex.Single

interface LotterySelectorContract {

    interface View {
        fun showLotteries(lotteries: List<Lottery>)
        fun emmitError(t: Throwable)
        fun emmitSelectedLottery(lottery: Lottery)
        fun emmitNoAvailableLotteriesError()
        fun emmitNoConnectionError()
        fun showNoLotterySelectedError()
        fun showLoading()
        fun hideLoading()
        fun hide()
    }

    interface Presenter {
        fun getAvailableLotteries()
        fun onButtonPressed(lotteries: List<Lottery>)
    }

    interface Interactor {
        fun requestAvailableLotteries(): Single<List<Lottery>>
    }
}