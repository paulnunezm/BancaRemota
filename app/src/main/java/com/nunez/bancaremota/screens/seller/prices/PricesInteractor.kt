package com.nunez.bancaremota.screens.seller.prices

import android.util.Log
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import com.nunez.bancaremota.framework.respository.data.User
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PricesInteractor(
        private val connectivityChecker: ConnectivityChecker,
        private val service: BancappService,
        private val androidScheduler: Scheduler
) : PricesContract.Interactor {

    override fun requestPrices(): Single<User> {
        Log.d("PricesInteractor","Request prices" )
        return if (connectivityChecker.isConected()) {
            service.getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
        } else {
            Single.error(NoConnectionException())
        }
    }
}