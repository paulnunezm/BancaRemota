package com.nunez.bancaremota.screens.seller.stats

import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class StatsInteractor(
        private val connectivyChecker: ConnectivityChecker,
        private val service: BancappService,
        private val scheduler: Scheduler

) : StatsContract.Interactor {

    override fun requestStats(): Single<Stats> {
        return if (connectivyChecker.isConected()) {
            service.getStats()
                    .subscribeOn(Schedulers.io())
                    .observeOn(scheduler)
                    .map {
                        require(it.success) { "Unsuccessful response" }
                        Stats(it.sales ?: "0.00",
                                it.commission ?: "0.00",
                                it.won ?: "0.00",
                                it.balance ?: "0.00")
                    }
        } else {
            Single.error(NoConnectionException())
        }
    }
}