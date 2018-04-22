package com.nunez.bancaremota.screens.seller.tickets

import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.respository.BancappService
import com.nunez.bancaremota.framework.respository.data.Ticket
import com.nunez.bancaremota.framework.respository.data.User
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

    private val compositeDisposable = CompositeDisposable()

    override fun getAllTickets() {
        ifIsConnected {
            hideEverythingAndShowLoading()
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
        ifIsConnected {
           hideEverythingAndShowLoading()
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
            hideEverythingAndShowLoading()
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
        view.hideLoading()
        if (response.success) {
            if (response.userStatus != User.ENABLED) {
                view.showUserBlockedError()
            } else {
                val tickets = response.tickets
                if (tickets.isNotEmpty()) {
                    view.showTickets(response.tickets)
                } else {
                    view.showNoTicketsMessage()
                }
            }
        } else {
            view.showUnexpectedError()
        }
    }

    private fun onError(t: Throwable) {
        view.hideLoading()
        view.showUnexpectedError()
    }

    private fun hideEverythingAndShowLoading(){
        view.hideTickets()
        view.showLoading()
        view.hideMessage()
    }
}