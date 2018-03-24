package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.LotteryResponse
import com.nunez.bancaremota.framework.respository.data.Lottery
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

class LotterySelectorInteractor(
        private val connectivityChecker: ConnectivityChecker,
        private val serviceProvider: ServiceProvider,
        private val androidScheduler: Scheduler
) : LotterySelectorContract.Interactor {

    override fun requestAvailableLotteries(): Single<List<Lottery>> {
        return if (connectivityChecker.isConected()) {
            val service = serviceProvider.getAuthorizedService()
            service.getAvailableLotteriesToSell()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .map(LotteryResponseMapper())

        } else {
            Single.error(NoConnectionException())
        }
    }

    private class LotteryResponseMapper : Function<LotteryResponse, List<Lottery>> {
        override fun apply(r: LotteryResponse): List<Lottery> {
            if (r.success) {
                return r.lotteries.map {
                    Lottery(it.id, it.name)
                }
            } else {
                throw Throwable("response not successful")
            }
        }
    }

}