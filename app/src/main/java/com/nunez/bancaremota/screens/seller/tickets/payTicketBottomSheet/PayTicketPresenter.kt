package com.nunez.bancaremota.screens.seller.tickets.payTicketBottomSheet

import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class PayTicketPresenter(
        private val view: PayTicketBottomSheetContract.View,
        private val connectivityChecker: ConnectivityChecker,
        private val service: BancappService,
        private val androidScheduler: Scheduler
) : PayTicketBottomSheetContract.Presenter {

    override fun makePayment(id: String) {
        if (connectivityChecker.isConected()) {
            view.showLoading()
            service.payTicket(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                        view.hideLoading()
                        if(it.success){
                            view.showPaymentSuccessful()
                        }else{
                            view.showPaymentUnsuccessful()
                        }
                    }, {
                        view.hideLoading()
                        view.showUnexpectedError()
                    })
        } else {
            view.showNoConnectionError()
        }
    }
}