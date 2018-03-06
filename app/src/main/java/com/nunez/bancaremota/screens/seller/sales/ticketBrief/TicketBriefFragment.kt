package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.helpers.ReceiptPrinter
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.screens.seller.sales.GamesAdapter
import com.nunez.palcine.BaseFragment
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.ticket_brief_fragment.*


class TicketBriefFragment : BaseFragment(), TicketBriefContract.View {

    override var layoutId: Int = R.layout.ticket_brief_fragment

    class ListOfGames(val games: List<Game>) // Helper class to serialize/deserialize the incoming data

    lateinit var interactor: TicketBriefContract.Interactor
    lateinit var presenter: TicketBriefContract.Presenter

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

        presenter.getPlaysAvailability(listOfGames.games)

        printButton.show()
        printButton.setOnClickListener { presenter.onPrintReceipt() }
    }

    override fun printReceipt(ticketInfo: TicketInfo, availablePlays: ArrayList<Game>) {
        ReceiptPrinter(activity, ticketInfo, availablePlays )
    }

    override fun showLoading() {
        Log.d(TAG, "show loading")
    }

    override fun hideLoading() {
        Log.d(TAG, "hide loading")
    }

    override fun showAvailablePlays(plays: List<Game>) {
        availablePlaysRecycler.apply {
            adapter = GamesAdapter(plays as ArrayList<Game>)
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun showUnavailablePlaysDialog(plays: List<Game>) {
        val fragmentManager = activity.supportFragmentManager
        val unavailableDialogFragment = UnavailablePlaysDialogFragment.newInstance(plays)
        unavailableDialogFragment.show(fragmentManager, "unavailable_dialog")
    }

    override fun showUnexpectedError() {
        Log.d(TAG, "show unexpected error")
    }

    override fun showConnectivityError() {
        Log.d(TAG, "show connectivity error")
    }
}