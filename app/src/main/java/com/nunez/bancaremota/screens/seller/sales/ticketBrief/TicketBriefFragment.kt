package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.MessageViewHandler
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.helpers.ReceiptPrinter
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.screens.seller.sales.GamesAdapter
import com.nunez.palcine.BaseActivity
import com.nunez.palcine.BaseFragment
import com.nunez.bancaremota.framework.extensions.gone
import com.nunez.bancaremota.framework.extensions.show
import com.nunez.bancaremota.framework.helpers.ConnectivityCheckerImpl
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.progress.*
import kotlinx.android.synthetic.main.ticket_brief_fragment.*


class TicketBriefFragment : BaseFragment(), TicketBriefContract.View {

    override var layoutId: Int = R.layout.ticket_brief_fragment

    class ListOfGames(val games: List<Game>) // Helper class to serialize/deserialize the incoming data

    lateinit var interactor: TicketBriefContract.Interactor
    lateinit var presenter: TicketBriefContract.Presenter
    lateinit var messageViewHandler: MessageViewHandler

    companion object {
        const val TAG = "TicketBrief"
        const val ARG_PLAYS = "plays"

        fun newInstance(plays: List<Game>): TicketBriefFragment {
            // Serialize to json
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(ListOfGames::class.java)
            val json = jsonAdapter.toJson(ListOfGames(plays))

            val args = Bundle()
            val fragment = TicketBriefFragment()
            args.putSerializable(ARG_PLAYS, json)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).changeTitle(activity.getString(R.string.action_bar_title_ticket_brief))

        val serializedPlays = arguments.getSerializable(ARG_PLAYS)
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(ListOfGames::class.java)
        val listOfGames = jsonAdapter.fromJson(serializedPlays.toString())

        val connectiviyChecker = ConnectivityCheckerImpl(activity)
        val prefManager = PreferencesManagerImpl(activity)
        val service = ServiceProvider(prefManager).getAuthorizedService()
        val androidScheduler = AndroidSchedulers.mainThread()

        interactor = TicketBriefInteractor(connectiviyChecker, service, androidScheduler)
        presenter = TicketBriefPresenter(this, interactor)

        messageViewHandler = MessageViewHandler(messageContainer)

        presenter.getPlaysAvailability(listOfGames.games)

        printButton.setOnClickListener { presenter.onPrintReceipt() }
    }

    override fun printReceipt(ticketInfo: TicketInfo, availablePlays: ArrayList<Game>) {
        ReceiptPrinter(activity, ticketInfo, availablePlays)
    }

    override fun showLoading() {
        content.gone()
        ticketView.gone()
        loadingView.show()
    }

    override fun hideLoading() {
        loadingView.gone()
    }

    override fun showAvailablePlays(plays: List<Game>) {
        content.show()
        ticketView.show()
        printButton.show()
        availablePlaysRecycler.apply {
            adapter = GamesAdapter(plays as ArrayList<Game>)
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    override fun showTicketNumber(newTicketNumber: String) {
        ticketNumber.text = newTicketNumber
    }

    override fun showTotalAmount(newTicketAmount: String) {
        totalAmount.text = activity.getString(R.string.format_amount, newTicketAmount)
    }

    override fun showUnavailablePlaysDialog(plays: List<Game>) {
        val fragmentManager = activity.supportFragmentManager
        val unavailableDialogFragment = UnavailablePlaysDialogFragment.newInstance(plays)
        unavailableDialogFragment.show(fragmentManager, "unavailable_dialog")
    }

    override fun showUnexpectedError() {
        messageViewHandler.showUnexpectedError()
    }

    override fun showConnectivityError() {
        messageViewHandler.showNoConnectionError()
    }

    override fun showNoAvailablePlaysError() {
        messageViewHandler.showNoAvailablePlays()
    }
}