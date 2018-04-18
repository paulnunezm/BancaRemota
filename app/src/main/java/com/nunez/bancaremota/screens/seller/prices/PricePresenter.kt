package com.nunez.bancaremota.screens.seller.prices

import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.framework.respository.data.UserSettings
import io.reactivex.disposables.CompositeDisposable

class PricePresenter(
        private val preferencesManager: PreferencesManager,
        private val view: PricesContract.View,
        private val interactor: PricesInteractor
) : PricesContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getPrices(prices: UserSettings) {
        view.showLoading()
        compositeDisposable.add(interactor.requestPrices()
                .subscribe({
                    if (it.status === User.DISABLED) {
                        onUserDisabled()
                    } else {
                        view.hideLoading()
                        view.showPrices(it.userSettings)
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