package com.nunez.bancaremota.screens.seller.stats

import android.os.Bundle
import android.util.Log
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.palcine.BaseFragment
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import kotlinx.android.synthetic.main.stats_fragment.*

class StatsFragment : BaseFragment(), StatsContract.View{
    override var layoutId: Int = R.layout.stats_fragment
    lateinit var presenter: StatsContract.Presenter

    companion object {
        private const val TAG = "StatsFragment"

        fun newInstance(): StatsFragment{
            return StatsFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = StatsPresenter(this, ConnectivityCheckerImpl(activity))

        refreshContainer.setOnRefreshListener { presenter.requestStats()}
        presenter.requestStats()
    }

    override fun showStats(stats: Stats) {
        refreshContainer.isRefreshing = false
        with(stats){
            salesValue.text = sales
            commissionValue.text = commission
            pricesValue.text = prices
            totalValue.text = total
        }
    }

    override fun showLoading() {
        Log.d(TAG, "ShowLoading")
    }

    override fun hideLoading() {
        Log.d(TAG, "hideLoading")
    }

    override fun showNoConnectionError() {
        Log.d(TAG, "showNoConnectionError")
    }

    override fun showUnexpectedError() {
        Log.d(TAG, "showUnexpectedError")
    }

}