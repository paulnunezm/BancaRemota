package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import com.nunez.bancaremota.screens.seller.sales.Lottery

class LotterySelectorPresenter(
        private val view: LotterySelectorContract.View,
        private val interactor: LotterySelectorContract.Interactor
) : LotterySelectorContract.Presenter {
    override fun getAvailableLotteries() {
        view.showLoading()
        interactor.requestAvailableLotteries().subscribe({
            if (it.isEmpty()) {
                view.emmitNoAvailableLotteriesError()
                view.hide()
            } else {
                view.hideLoading()
                view.showLotteries(it)
            }
        }, {
            view.emmitError(it)
            view.hide()
        })
    }

    override fun onButtonPressed(lotteries: List<Lottery>) {
        val selected = lotteries.filter {
            it.selected
        }

        if(selected.isEmpty()){
            view.showNoLotterySelectedError()
        }else{
            selected.forEach {
                view.emmitSelectedLottery(it)
            }
            view.hide()
        }
    }
}