package com.nunez.bancaremota.screens.seller.winningNumbers

interface WinningNumbersContract {

    interface View {
        fun hideLoading()
        fun showLoading()
        fun showNoConnectionError()
        fun showNumbers(numbers: List<WinningNumbers>)
        fun showUnexpectedError()
    }

    interface Presenter {
        fun requestWinningNumbers()
    }
}