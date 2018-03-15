package com.nunez.bancaremota.screens.seller.tickets.ticketDetails

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.FormatterHelper
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.framework.respository.data.Ticket
import com.nunez.bancaremota.screens.seller.sales.GamesAdapter
import com.nunez.bancaremota.screens.seller.tickets.payTicketBottomSheet.PayTicketBottomSheet
import com.nunez.palcine.BaseActivity
import com.nunez.palcine.framework.extensions.hide
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.ticket_details.*

class TicketDetails : BaseActivity(), TicketDetailsContract.View {

    override var toolbarId: Int? = R.id.toolbar
    override var drawerId: Int? = null
    override var layout: Int = R.layout.ticket_details

    companion object {
        const val EXTRA_TICKET = "extra_ticket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = intent.getStringExtra(EXTRA_TICKET)
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(Ticket::class.java)
        val ticket = jsonAdapter.fromJson(extras.toString())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = getString(R.string.ticket_msg_details)
        }

        val presenter = TicketDetailsPresenter(this, ticket)
        presenter.showTicket()

        payButton.setOnClickListener { presenter.onPayClicked() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hidePayButton() {
        payButton.hide()
    }

    override fun hidePrintButton() {
        printButton.hide()
    }

    override fun showPlays(plays: List<Game>) {
        gamesRecycler.apply {
            layoutManager = LinearLayoutManager(this@TicketDetails)
            setHasFixedSize(true)
            adapter = GamesAdapter(ArrayList(plays))
            addItemDecoration(DividerItemDecoration(this@TicketDetails, DividerItemDecoration.HORIZONTAL))
        }
    }

    override fun showNumberAndAmount(number: String, amount: String) {
        ticket_number.text = FormatterHelper.getFormattedTicketNumber(number)
        ticket_amount.text = amount
    }

    override fun showBlockedStatus() {
        ticket_status.text = getString(R.string.ticket_status_blocked)
    }

    override fun showPaidStatus() {
        ticket_status.text = getString(R.string.ticket_status_paid)
    }

    override fun showPrintedStatus() {
        ticket_status.text = getString(R.string.ticket_status_printed)
    }

    override fun showUnprintedStatus() {
        ticket_status.text = getString(R.string.ticket_status_unprinted)
    }

    override fun showWinnerStatus() {
        ticket_status.text = getString(R.string.ticket_status_winner)
    }

    override fun showPaymentBottomSheet(id: String, currency: String, amountToPay: String) {
        val bottomSheet = PayTicketBottomSheet.newInstance(id, currency, amountToPay)
            bottomSheet.show(supportFragmentManager, "pay_bottom_sheet")
    }
}