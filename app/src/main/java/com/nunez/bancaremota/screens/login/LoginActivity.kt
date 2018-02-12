package com.nunez.bancaremota.screens.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.screens.seller.SellerActivity
import com.nunez.palcine.framework.helpers.ConnectivityCheckerImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.login_activity.*

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
        showToast("showLoginError")
    }

    override fun showUserBlockedError() {
        showToast("showUserBlockedError")
    }

    override fun showNoConnectionError() {
        showToast("showNoConnectionError")
    }

    override fun showUnexpectedError() {
        showToast("unexpectedError")
    }

    override fun showLoading() {
        showToast("loading")
    }

    override fun hideLoading() {
        showToast("hideLoading")
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
