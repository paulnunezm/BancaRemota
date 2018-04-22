package com.nunez.bancaremota.screens.seller.prices

import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.respository.data.User
import io.reactivex.disposables.CompositeDisposable

class PricePresenter(
        private val preferencesManager: PreferencesManager,
        private val view: PricesContract.View,
        private val interactor: PricesContract.Interactor
) : PricesContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getPrices() {
        view.showLoading()
        compositeDisposable.add(interactor.requestPrices()
                .subscribe({
                    if (it.status === User.DISABLED) {
                        onUserDisabled()
                    } else {
                        view.hideLoading()
                        it?.userSettings?.let {
                            view.showPrices(it)
                        }
                    }

                }, {
                    view.hideLoading()
                    if (it is NoConnectionException) {
                        view.showNoConnectionError()
                    } else {
                        view.showUnexpectedError()
                    }

                }))

    }

    private fun onUserDisabled() {
        preferencesManager.logout()
        view.goToLoginActivity()
    }

}