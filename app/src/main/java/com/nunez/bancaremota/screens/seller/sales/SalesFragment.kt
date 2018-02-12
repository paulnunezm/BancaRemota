package com.nunez.bancaremota.screens.seller.sales

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.LinearLayout
import com.nunez.bancaremota.R
import com.nunez.bancaremota.screens.seller.sales.lotterySelector.LotterySelector
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.TicketBriefFragment
import com.nunez.palcine.BaseFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.sales_fragment.*
import java.util.*

class SalesFragment : BaseFragment(), SalesContract.View {

    private val TAG = this::class.java.simpleName

    override var layoutId: Int = R.layout.sales_fragment

    private lateinit var presenter: SalesContract.Presenter
    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var lotterySelector: LotterySelector
    private lateinit var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback
    private lateinit var itemTouchHelper: ItemTouchHelper
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

        presenter = SalesPresenter(this)
        presenter.apply {
            observeGameEntry()
            observeLotteryEntry()
        }

        processOrder.setOnClickListener {
            presenter.onSellButtonPressed()
        }

        addSwipeListener()
    }

    override fun addPlay(game: Game) {
        gameList.add(game)
        refreshData()
    }

    override fun goToTicketBriefFragment(plays: List<Game>) {
        val fragment = TicketBriefFragment.newInstance(plays)
        fragmentManager.beginTransaction()
                .addToBackStack("sales_fragment")
                .replace(activity.contentFrame.id, fragment)
                .commit()
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

    override fun showErasedPlayMessage() {
    }

    override fun showLoading() {
    }

    override fun showNoAvailableLotteriesError() {
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

    override fun removeItemFromList(position: Int) {
        gamesAdapter.onItemDismissed(position)
    }

    private fun addSwipeListener(){
        itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val position = viewHolder?.adapterPosition
                position?.let {
                    presenter.onItemSwipe(position)
                }
            }
        }
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}
