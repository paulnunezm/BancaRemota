package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import com.nunez.bancaremota.framework.respository.data.Lottery
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LotterySelectorPresenter(
        private val view: LotterySelectorContract.View,
        private val interactor: LotterySelectorContract.Interactor
) : LotterySelectorContract.Presenter {
    override fun getAvailableLotteries() {
        view.showLoading()
        interactor.requestAvailableLotteries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
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