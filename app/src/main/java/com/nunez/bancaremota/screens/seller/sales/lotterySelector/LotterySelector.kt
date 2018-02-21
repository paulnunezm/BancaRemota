package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.exceptions.NotAvailableLotteriesException
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.Lottery
import com.nunez.palcine.framework.exceptions.NoConnectionException
import com.nunez.palcine.framework.helpers.ConnectivityChecker
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.lottery_selector_bottom_sheet.*

class LotterySelector : BottomSheetDialogFragment(), ILotterySelector {

    private lateinit var presenter: LotterySelectorContract.Presenter
    private lateinit var interactor: LotterySelectorContract.Interactor
    private lateinit var connectivityChecker: ConnectivityChecker
    private lateinit var serviceProvider: ServiceProvider
    private lateinit var preferenceManager: PreferencesManager

    private lateinit var lotteriesAdapter: LotteriesAdapter
    override var selectedLotteries: PublishSubject<Lottery> = PublishSubject.create()

    companion object {
        fun newInstance(): LotterySelector {
            return LotterySelector()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val scheduler = AndroidSchedulers.mainThread()
        preferenceManager = PreferencesManagerImpl(activity)
        connectivityChecker = ConnectivityCheckerImpl(activity)
        serviceProvider = ServiceProvider(preferenceManager)
        interactor = LotterySelectorInteractor(connectivityChecker, serviceProvider, scheduler)
        presenter = LotterySelectorPresenter(this, interactor)

        return inflater?.inflate(R.layout.lottery_selector_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        lotteriesAdapter = LotteriesAdapter(emptyList())

        presenter.getAvailableLotteries()
        selectBtn.setOnClickListener {
            presenter.onButtonPressed(lotteriesAdapter.items)
        }
        super.onViewCreated(view, savedInstanceState)
    }


    override fun emmitSelectedLottery(lottery: Lottery) {
        selectedLotteries.onNext(lottery)
    }

    override fun showLotteries(lotteries: List<Lottery>) {
        lotteriesAdapter = LotteriesAdapter(lotteries)
        recycler.apply {
            adapter = lotteriesAdapter
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
        }
    }

    override fun emmitError(t: Throwable) {
        selectedLotteries.onError(t)
    }

    override fun hide() {
        dismiss()
    }

    override fun emmitNoAvailableLotteriesError() {
        selectedLotteries.onError(NotAvailableLotteriesException())
    }

    override fun emmitNoConnectionError() {
        selectedLotteries.onError(NoConnectionException())
    }

    override fun showNoLotterySelectedError() {
        Toast.makeText(activity, "¡Debe de seleccionar al menos una lotería!", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Toast.makeText(activity, "LoadingFinished", Toast.LENGTH_SHORT).show()
    }
}
interface ILotterySelector : LotterySelectorContract.View {
    var selectedLotteries: PublishSubject<Lottery>
}

