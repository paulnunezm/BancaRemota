package com.nunez.bancaremota.screens.seller.stats

import android.os.Bundle
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.extensions.gone
import com.nunez.bancaremota.framework.extensions.show
import com.nunez.bancaremota.framework.helpers.ConnectivityCheckerImpl
import com.nunez.bancaremota.framework.helpers.MessageViewHandler
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.palcine.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.progress.*
import kotlinx.android.synthetic.main.stats_fragment.*

class StatsFragment : BaseFragment(), StatsContract.View{
    override var layoutId: Int = R.layout.stats_fragment
    lateinit var presenter: StatsContract.Presenter

    lateinit var messageViewHandler : MessageViewHandler

    companion object {
        private const val TAG = "StatsFragment"

        fun newInstance(): StatsFragment{
            return StatsFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferencesManager = PreferencesManagerImpl(this.activity)
        val serviceProvider = ServiceProvider(preferencesManager)
        val authService  = serviceProvider.getAuthorizedService()
        val connectivyChecker = ConnectivityCheckerImpl(this.activity)
        val interactor = StatsInteractor(connectivyChecker, authService, AndroidSchedulers.mainThread())
        presenter = StatsPresenter(this, interactor)

        messageViewHandler = MessageViewHandler(messageContainer)

        refreshContainer.setOnRefreshListener { presenter.requestStats()}
        presenter.requestStats()
    }

    override fun showStats(stats: Stats) {
        refreshContainer.isRefreshing = false
        with(stats){
            salesValue.text = sales
            commissionValue.text = commission
            pricesValue.text = won
            totalValue.text = total
        }
        content.show()
    }

    override fun showLoading() {
        loadingView.show()
        content.gone()
    }

    override fun hideLoading() {
        loadingView.gone()
    }

    override fun showNoConnectionError() {
        refreshContainer.isRefreshing = false
        loadingView.gone()
        messageViewHandler.showNoConnectionError()
    }

    override fun showUnexpectedError() {
        refreshContainer.isRefreshing = false
        messageViewHandler.showUnexpectedError()
    }
}