package com.nunez.bancaremota.screens.seller.stats

import com.nunez.palcine.framework.helpers.ConnectivityChecker

class StatsPresenter(
        private val view: StatsContract.View,
        private val connectivityChecker: ConnectivityChecker
) : StatsContract.Presenter {

    var first = false

    override fun requestStats() {
        val stats = if (first) {
            Stats("1,000","500","100", "300")
        } else {
            first = true
            Stats("2,000","500","100", "300")
        }
        view.showStats(stats)
    }
}