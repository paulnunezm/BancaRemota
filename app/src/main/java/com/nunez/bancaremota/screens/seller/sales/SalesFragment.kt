package com.nunez.bancaremota.screens.seller.sales

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.screens.seller.sales.lotterySelector.LotterySelector
import com.nunez.palcine.BaseFragment
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.sales_fragment.*
import java.util.*

class SalesFragment : BaseFragment(), SalesContract.View {

    override var layoutId: Int = R.layout.sales_fragment

    private lateinit var presenter: SalesContract.Presenter
    private lateinit var interactor: SalesContract.Interactor
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var lotterySelector: LotterySelector
    private var gameList = ArrayList<Game>()

    companion object {
        fun newInstance(): SalesFragment {
            return SalesFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
            setHasFixedSize(true)
        }

        lotterySelector = LotterySelector()

        val preferencesManager = PreferencesManagerImpl(activity)
        val serviceProvider = ServiceProvider(preferencesManager)
        val service = serviceProvider.getAuthorizedService()
        interactor = SalesInteractor(ConnectivityCheckerImpl(activity),service, AndroidSchedulers.mainThread())
        presenter = SalesPresenter(this, interactor)
        presenter.apply {
            observeGameEntry()
            observeLotteryEntry()
        }

        processOrder.setOnClickListener {
            presenter.onSellButtonPressed()
        }
    }

    override fun addPlay(game: Game) {
        gameList.add(game)
        refreshData()
    }

    override fun showLotteriesSelector() {
        lotterySelector.show(activity.supportFragmentManager, "selector")
    }

    override fun observeGameEntry(): Observable<Game> {
        return gameEntry.onGameEntered
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun observeSelectedLotteries(): Observable<Lottery> {
        return lotterySelector.selectedLotteries
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun showUserBlockedError() {
    }

    override fun showNoConnectionError() {
    }

    override fun showUnexpectedError() {
    }

    override fun showLoading() {
    }

    override fun showProcessOrderButton() {
        processOrder.show()
    }

    override fun hideProcessOrderButton() {
        processOrder.hide()
    }

    private fun refreshData() {
        gamesAdapter = GamesAdapter(gameList)
        recycler.adapter = gamesAdapter
        gamesAdapter.notifyDataSetChanged()
    }
}
