package com.nunez.bancaremota.screens.login

import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import io.reactivex.disposables.CompositeDisposable

class LoginPresenter(
        private val view: LoginContract.View,
        private val interactor: LoginContract.Interactor
) : LoginContract.Presenter {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var accessTokenResponse: AccessTokenResponse

    override fun onLoginButtonPressed(username: String, password: String) {
        view.showLoading()
        compositeDisposable.add(interactor.requestAccessToken(username, password)
                .subscribe(
                        this::onAccessTokenReceived,
                        this::onAuthenticationError)
        )
    }

    private fun onAccessTokenReceived(accessToken: AccessTokenResponse) {
        accessTokenResponse = accessToken
        interactor.saveToken(accessTokenResponse)

        compositeDisposable.add(interactor.requestUserInfo(accessToken)
                .subscribe(
                        this::onUserInfoReceived,
                        this::onUnexpectedError
                ))
    }

    private fun onAuthenticationError(t: Throwable) {
        if (t is NoConnectionException) {
            view.showNoConnectionError()
        } else {
            view.showLoginError()
        }
        view.hideLoading()
    }

    private fun onUserInfoReceived(userInfo: User) {
        when (userInfo.status) {
            User.ENABLED -> onEnabledUserReceived(userInfo)
            User.DISABLED -> {
                view.hideLoading()
                view.showUserBlockedError()
            }
        }
    }

    private fun onUnexpectedError(t: Throwable) {
        if (t is NoConnectionException) {
            view.showNoConnectionError()
        } else {
            view.showUnexpectedError()
        }
        view.hideLoading()
    }

    private fun onEnabledUserReceived(userInfo: User) {
        compositeDisposable.add(interactor.saveUserInfo(userInfo, accessTokenResponse)
                .subscribe(
                        {
                            view.goToSellerActivity()
                        },
                        {
                            view.hideLoading()
                            view.showUnexpectedError()
                        }))
    }
}

