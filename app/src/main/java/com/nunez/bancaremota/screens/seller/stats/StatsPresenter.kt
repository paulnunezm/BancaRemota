package com.nunez.bancaremota.screens.seller.stats

import android.util.Log
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import io.reactivex.disposables.CompositeDisposable

class StatsPresenter(
        private val view: StatsContract.View,
        private val interactor: StatsContract.Interactor
) : StatsContract.Presenter {

    private val disposable = CompositeDisposable()

    override fun requestStats() {
        view.showLoading()
        disposable.add(interactor.requestStats()
                .subscribe(
                        {
                            view.hideLoading()
                            view.showStats(it)
                        },
                        this::onError)
        )
    }

    private fun onError(t: Throwable) {
        view.hideLoading()

        when (t) {
            is NoConnectionException -> view.showNoConnectionError()
            else -> {
                Log.d("presenter", "illegal state ")
                view.showUnexpectedError()
            }
        }
    }
}