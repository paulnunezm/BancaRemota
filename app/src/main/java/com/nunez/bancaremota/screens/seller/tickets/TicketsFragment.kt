package com.nunez.bancaremota.screens.seller.tickets

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.Ticket
import com.nunez.bancaremota.screens.seller.tickets.ticketDetails.TicketDetails
import com.nunez.palcine.BaseActivity
import com.nunez.palcine.BaseFragment
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import com.squareup.moshi.Moshi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.tickets_fragment.*


class TicketsFragment : BaseFragment(), TicketsContract.View {

    override var layoutId: Int = R.layout.tickets_fragment

    private lateinit var searchView: SearchView
    private lateinit var presenter: TicketsContract.Presenter
    private val searchQuerySubject: PublishSubject<String> = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val preferenceManager = PreferencesManagerImpl(activity)
        val serviceProvider = ServiceProvider(preferenceManager)
        val service = serviceProvider.getAuthorizedService()
        val connectivityChecker = ConnectivityCheckerImpl(activity)
        presenter = TicketsPresenter(this, service, AndroidSchedulers.mainThread(), connectivityChecker)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).changeTitle(activity.getString(R.string.action_bar_title_tickets))

        ticketsRecycler.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        presenter.getAllTickets()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuInflater = activity.menuInflater
        menuInflater.inflate(R.menu.tickets_fragment, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText.isNotEmpty()){
                    searchQuerySubject.onNext(newText)
                }else{
                    presenter.getAllTickets()
                }
                return true
            }
        })

        presenter.subscribeToSearchQueryChanges(searchQuerySubject)


        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun goToTicketDetails(ticket: Ticket) {
        val intent = Intent(activity, TicketDetails::class.java)

        // Serialize ticket to json
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Ticket::class.java)
        val json = jsonAdapter.toJson(ticket)
        intent.putExtra(TicketDetails.EXTRA_TICKET, json)

        startActivity(intent)
    }

    override fun showNoConnectionError() {
    }

    override fun showUserBlockedError() {
    }

    override fun showTickets(tickets: List<Ticket>) {
        ticketsRecycler.adapter = TicketsAdapter(activity, tickets, {
            presenter.ticketClicked(it)
        })
    }

    override fun showUnexpectedError() {
    }

    override fun showLoading() {
        Log.d("TicketsFragment", "ShowLoading")
    }

    override fun hideLoading() {
        Log.d("TicketsFragment", "HideLoading")
    }
}