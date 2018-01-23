package com.nunez.bancaremota.screens.login

import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User
import io.reactivex.Completable
import io.reactivex.Single

interface LoginContract {

    interface View {
        fun showLoginError()
        fun showUserBlockedError()
        fun showNoConnectionError()
        fun showUnexpectedError()
        fun showLoading()
        fun hideLoading()
        fun goToSellerActivity()
    }

    interface Presenter {
        fun onLoginButtonPressed(username: String, password: String)
    }

    interface Interactor {
        fun requestAccessToken(username: String, passwor: String): Single<AccessTokenResponse>
        fun requestUserInfo(accessTokenResponse: AccessTokenResponse): Single<User>
        fun saveUserInfo(user: User, accessToken: AccessTokenResponse): Completable
        fun saveToken(accessTokenResponse: AccessTokenResponse)
    }
}