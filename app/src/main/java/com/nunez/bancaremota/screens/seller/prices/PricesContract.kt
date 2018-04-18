package com.nunez.bancaremota.screens.seller.prices

import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.framework.respository.data.UserSettings
import io.reactivex.Single

interface PricesContract {

    interface View {
        fun goToLoginActivity()
        fun hideLoading()
        fun showNoConnectionError()
        fun showUnexpectedError()
        fun showLoading()
        fun showPrices(userSettings: UserSettings)
    }

    interface Presenter {
        fun getPrices(prices: UserSettings)
    }

    interface Interactor {
        fun requestPrices(): Single<User>
    }
}