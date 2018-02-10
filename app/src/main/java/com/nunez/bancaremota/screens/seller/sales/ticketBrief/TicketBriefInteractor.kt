package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import android.util.Log
import com.nunez.bancaremota.screens.seller.sales.Game
import com.nunez.palcine.framework.exceptions.NoConnectionException
import com.nunez.palcine.framework.helpers.ConnectivityChecker
import com.nunez.palcine.framework.respository.BancappService
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

    class PostTicket(val games: List<Game>)
}