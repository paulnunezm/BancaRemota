package com.nunez.bancaremota.screens.seller.tickets.payTicketBottomSheet

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.views.MessageHandler
import com.nunez.bancaremota.framework.extensions.gone
import com.nunez.bancaremota.framework.extensions.show
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.message.*
import kotlinx.android.synthetic.main.progress.*
import kotlinx.android.synthetic.main.ticket_pay_bottom_sheeet.*
import kotlinx.android.synthetic.main.ticket_pay_bottom_sheet_payment_info.*


class PayTicketBottomSheet : BottomSheetDialogFragment(), PayTicketBottomSheetContract.View {

    private lateinit var presenter: PayTicketBottomSheetContract.Presenter
    private lateinit var connectivityChecker: ConnectivityChecker
    private lateinit var serviceProvider: ServiceProvider
    private lateinit var preferenceManager: PreferencesManager
    private lateinit var messageHandler: MessageHandler


    companion object {
        private const val EXTRA_AMOUNT_TO_PAY = "extra_amount"
        private const val EXTRA_CURRENCY = "extra_currency"
        private const val EXTRA_ID = "extra_id"

        fun newInstance(ticketId: String, currency: String, amountToPay: String): PayTicketBottomSheet {
            val bundle = Bundle()
            bundle.putString(EXTRA_ID, ticketId)
            bundle.putString(EXTRA_CURRENCY, currency)
            bundle.putString(EXTRA_AMOUNT_TO_PAY, amountToPay)

            val fragment = PayTicketBottomSheet()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        preferenceManager = PreferencesManagerImpl(activity)
        connectivityChecker = ConnectivityCheckerImpl(activity)
        serviceProvider = ServiceProvider(preferenceManager)
        val service = serviceProvider.getAuthorizedService()
        val scheduler = AndroidSchedulers.mainThread()
        presenter = PayTicketPresenter(this, connectivityChecker, service, scheduler)

        return inflater?.inflate(R.layout.ticket_pay_bottom_sheeet, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageHandler = MessageHandler(activity, messageIcon, messageText)

        val currency = arguments.getString(EXTRA_CURRENCY)
        val amount = arguments.getString(EXTRA_AMOUNT_TO_PAY)
        currencyTxt.text = currency
        amountTxt.text = amount

        cancelButton.setOnClickListener { dismiss() }
        payButton.setOnClickListener {
            presenter.makePayment(arguments.getString(EXTRA_ID))
        }
    }

    override fun showLoading() {
        loadingView.show()
        infoContainer.gone()
    }

    override fun hideLoading() {
        loadingView.gone()
    }

    override fun showUnexpectedError() {
        infoContainer.gone()
        messageHandler.setUnexpectedErrorMessage()
        messageContainer.show()
    }

    override fun showNoConnectionError() {
        infoContainer.gone()
        messageHandler.setNoConnectionError()
        messageContainer.show()
    }

    override fun showPaymentSuccessful() {
        infoContainer.gone()
        successContainer.show()
    }

    override fun showPaymentUnsuccessful() {
        infoContainer.gone()
        messageHandler.setCustomMessage(R.string.ticket_pay_bottom_sheet_error_msg_payment_unsuccessful,0)
        messageContainer.show()
    }
}

