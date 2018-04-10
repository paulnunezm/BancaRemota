package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import android.util.Log
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import com.nunez.bancaremota.framework.respository.data.Game
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TicketBriefInteractor(
        val connectivityChecker: ConnectivityChecker,
        val service: BancappService,
        val androidScheduler: Scheduler
) : TicketBriefContract.Interactor {

    override fun checkPlaysAvailability(plays: List<Game>): Single<PlayAvailabilityResponse> {

        if (connectivityChecker.isConected()) {
            val ticket = PostTicket(plays)
            return Single.create { emitter ->
                service.postPlays(ticket)
                        .subscribeOn(Schedulers.io())
                        .observeOn(androidScheduler)
                        .subscribe({
                            emitter.onSuccess(it)
                        }, {
                            emitter.onError(it)
                            Log.e("TicketBriefInteractor", it.message, it)
                        })
            }
        } else {
            return Single.error(NoConnectionException())
        }

    }

    override fun printTicket(id: String): Single<PrintResponse> {
        if(connectivityChecker.isConected()){
            return service.printTicket(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
        }else{
            return Single.error(NoConnectionException())
        }
    }

    class PostTicket(val games: List<Game>)
}