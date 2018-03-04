package com.nunez.bancaremota.screens.seller.tickets.payTicketBottomSheet

interface PayTicketBottomSheetContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showUnexpectedError()
        fun showNoConnectionError()
        fun showPaymentSuccessful()
        fun showPaymentUnsuccessful()
    }

    interface Presenter {
        fun makePayment(id: String)
    }
}