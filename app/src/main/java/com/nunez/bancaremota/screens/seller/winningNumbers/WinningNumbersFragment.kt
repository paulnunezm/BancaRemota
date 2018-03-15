package com.nunez.bancaremota.screens.seller.winningNumbers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.palcine.BaseActivity
import com.nunez.palcine.BaseFragment
import com.nunez.palcine.framework.extensions.gone
import com.nunez.palcine.framework.extensions.show
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.progress.*
import kotlinx.android.synthetic.main.winning_numbers.*

class WinningNumbersFragment: BaseFragment(), WinningNumbersContract.View {

    override var layoutId: Int = R.layout.winning_numbers

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferencesManager = PreferencesManagerImpl(activity)
        val connectivityChecker = ConnectivityCheckerImpl(activity)
        val service = ServiceProvider(preferencesManager).getAuthorizedService()
        val androidScheduler = AndroidSchedulers.mainThread()
        val presenter = WinningNumbersPresenter(this, connectivityChecker, service, androidScheduler)
        presenter.requestWinningNumbers()

        (activity as BaseActivity).changeTitle(activity.getString(R.string.action_bar_title_winning_numbers))
    }

    override fun hideLoading() {
        loadingView.gone()
    }

    override fun showLoading() {
        loadingView.show()
    }

    override fun showNoConnectionError() {
    }

    override fun showNumbers(numbers: List<WinningNumbers>) {
        recycler.apply {
            adapter = WinningNumbersAdapter(activity, numbers)
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun showUnexpectedError() {
    }
}