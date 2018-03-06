package com.nunez.bancaremota.screens.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.screens.seller.SellerActivity
import com.nunez.palcine.framework.extensions.gone
import com.nunez.palcine.framework.extensions.show
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.progress.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter
    private lateinit var interactor: LoginInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val preferencesManager = PreferencesManagerImpl(this)

        interactor = LoginInteractor(
                ConnectivityCheckerImpl(this),
                ServiceProvider(preferencesManager),
                AndroidSchedulers.mainThread(),
                preferencesManager)

        presenter = LoginPresenter(this, interactor)

        loginButton.setOnClickListener {
            presenter.onLoginButtonPressed(usernameInput.text.toString(),
                    passwordInput.text.toString())
        }
    }

    override fun showLoginError() {
      Snackbar.make(container, getString(R.string.error_message_login), Snackbar.LENGTH_LONG).show()
    }

    override fun showUserBlockedError() {
        Snackbar.make(container, getString(R.string.error_message_blocked_user), Snackbar.LENGTH_LONG).show()
    }

    override fun showNoConnectionError() {
        Snackbar.make(container, getString(R.string.error_message_no_connection), Snackbar.LENGTH_LONG).show()
    }

    override fun showUnexpectedError() {
        Snackbar.make(container, getString(R.string.error_message_unexpected_error), Snackbar.LENGTH_LONG).show()
    }

    override fun showLoading() {
        card.gone()
        loadingView.show()
    }

    override fun hideLoading() {
        card.show()
        loadingView.gone()
    }

    override fun goToSellerActivity() {
        val intent = Intent(this, SellerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

}
