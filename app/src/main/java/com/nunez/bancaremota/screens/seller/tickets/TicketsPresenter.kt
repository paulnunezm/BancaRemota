package com.nunez.bancaremota.screens.seller.tickets

import android.util.Log
import com.nunez.bancaremota.framework.respository.data.Ticket
import com.nunez.palcine.framework.helpers.ConnectivityChecker
import com.nunez.palcine.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TicketsPresenter(
        private val view: TicketsContract.View,
        private val service: BancappService,
        private val androidScheduler: Scheduler,
        private val connectivityChecker: ConnectivityChecker
) : TicketsContract.Presenter {

    private lateinit var tickets: List<Ticket>
    private val compositeDisposable = CompositeDisposable()

    override fun getAllTickets() {
        Log.d("TicketsPresenter", "getAllTickets")

        ifIsConnected {
            view.hideTickets()
            view.showLoading()
            compositeDisposable.add(service.getTodayTickets()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                       onTicketResponse(it)
                    }, {
                        onError(it)
                    }))
        }
    }

    override fun subscribeToSearchQueryChanges(subject: PublishSubject<String>) {
        Log.d("Presenter", "subscribeToSearch")
        ifIsConnected {
            view.hideTickets()
            view.showLoading()
            compositeDisposable.add(
                    subject
                            .debounce(300, TimeUnit.MILLISECONDS)
                            .filter { it.isNotEmpty() }
                            .distinctUntilChanged()
                            .switchMap {
                                service.searchTicket(it).toObservable()
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(androidScheduler)
                            .subscribe({
                                onTicketResponse(it)
                            }, {
                                onError(it)
                            }))
        }
    }

    override fun getTicketsWithId(id: String) {
        ifIsConnected {
            view.showLoading()
            compositeDisposable.add(service.searchTicket(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                        onTicketResponse(it)
                    }, {
                        onError(it)
                    }))
        }
    }

    private fun isConnected(): Boolean {
        return if (connectivityChecker.isConected()) {
            true
        } else {
            view.showNoConnectionError()
            false
        }
    }

    override fun ticketClicked(ticket: Ticket) {
        view.goToTicketDetails(ticket)
    }


    private fun ifIsConnected(body: () -> Unit) {
        if (connectivityChecker.isConected()) {
            body()
        } else {
            view.showNoConnectionError()
        }
    }

    private fun onTicketResponse(response: TicketsResponse) {
        Log.d("TicketsPresenter", response.toString())
        view.hideLoading()
        if (response.success) {
            if (response.userStatus != "enabled") {
                view.showUserBlockedError()
            } else {
                view.showTickets(response.tickets)
            }
        } else {
            view.showUnexpectedError()
        }
    }

    private fun onError(t: Throwable) {
        view.hideLoading()
        view.showUnexpectedError()
    }
}