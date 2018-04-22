package com.nunez.bancaremota.screens.seller.winningNumbers

import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class WinningNumbersPresenter(
        private val view: WinningNumbersContract.View,
        private val connectivityChecker: ConnectivityChecker,
        private val service: BancappService,
        private val androidScheduler: Scheduler
) : WinningNumbersContract.Presenter {

    override fun requestWinningNumbers() {
        view.showLoading()
        if (connectivityChecker.isConected()) {
            service.getWinningNumbers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                        view.hideLoading()
                        if (it.success) {
                            if (it.numbers.isEmpty()) {
                                view.showNoWinningNumbers()
                            } else {
                                view.showNumbers(it.numbers)
                            }
                        } else {
                            view.showUnexpectedError()
                        }
                    }, {
                        view.hideLoading()
                        view.showUnexpectedError()
                    })
        } else {
            view.hideLoading()
            view.showNoConnectionError()
        }
    }

}