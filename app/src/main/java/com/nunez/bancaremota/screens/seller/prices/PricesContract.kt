package com.nunez.bancaremota.screens.seller.prices

import com.nunez.bancaremota.framework.respository.data.User
import io.reactivex.Single

interface PricesContract {

    interface View {
        fun showUserBlockedError()
        fun showNoConnectionError()
        fun showUnexpectedError()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun getPrices()
    }

    interface Interactor {
        fun requestPrices(): Single<User>
    }
}