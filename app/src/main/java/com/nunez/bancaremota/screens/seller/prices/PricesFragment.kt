package com.nunez.bancaremota.screens.seller.prices

import android.os.Bundle
import android.view.View
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.ConnectivityCheckerImpl
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.UserSettings
import com.nunez.palcine.BaseActivity
import com.nunez.palcine.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.price_pale.*
import kotlinx.android.synthetic.main.price_quiniela.*
import kotlinx.android.synthetic.main.price_tripleta.*

class PricesFragment : BaseFragment(), PricesContract.View {

    override var layoutId: Int = R.layout.price_fragment

    lateinit var interactor: PricesContract.Interactor
    lateinit var presenter: PricesContract.Presenter

    companion object {
        fun newInstance(): PricesFragment {
            return PricesFragment()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as BaseActivity).changeTitle(getString(R.string.seller_screen_drawer_item_prices))

        val connectivityChecker = ConnectivityCheckerImpl(activity)
        val prefsManager = PreferencesManagerImpl(activity)
        val serviceProvider = ServiceProvider(prefsManager)
        val bancappService = serviceProvider.getAuthorizedService()
        interactor = PricesInteractor(connectivityChecker, bancappService, AndroidSchedulers.mainThread())
        presenter = PricePresenter(prefsManager, this, interactor)

        presenter.getPrices()
    }

    override fun goToLoginActivity() {
    }

    override fun hideLoading() {
    }

    override fun showNoConnectionError() {
    }

    override fun showUnexpectedError() {
    }

    override fun showLoading() {
    }

    override fun showPrices(userSettings: UserSettings) {
        with(userSettings) {
            quinielaFirstValue.text = quinielaFirst
            quinielaSecondValue.text = quinielaSecond
            quinielaThirdValue.text = quinielaThird
            paleFirstValue.text = paleFirst
            paleSecondValue.text = paleSecond
            tripletaFirstValue.text = tripletaFirst
            tripletaSecondValue.text = tripletaSecond
        }
    }

}