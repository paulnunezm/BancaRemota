package com.nunez.bancaremota.screens.seller.stats

import io.reactivex.Single

interface StatsContract {

    interface View{
        fun showStats(stats: Stats)
        fun showLoading()
        fun hideLoading()
        fun showNoConnectionError()
        fun showUnexpectedError()
    }

    interface Presenter{
        fun requestStats()
    }

    interface Interactor{
        fun requestStats(): Single<Stats>
    }
}