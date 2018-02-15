package com.nunez.bancaremota.screens.seller.winningNumbers

import com.nunez.palcine.framework.helpers.ConnectivityChecker
import com.nunez.palcine.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class WinningNumbersPresenter(
        private val view: WinningNumbersContract.View,
        private val connectivityChecker: ConnectivityChecker,
        private val service: BancappService,
        private val androidScheduler: Scheduler
) : WinningNumbersContract.Presenter {

    override fun requestWinningNumbers() {
        if(connectivityChecker.isConected()){
            service.getWinningNumbers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                        if(it.success){
                            view.showNumbers(it.numbers)
                        }else{
                            view.showUnexpectedError()
                        }
                    },{
                        view.showUnexpectedError()
                    })
        }else{
            view.showNoConnectionError()
        }
    }

}