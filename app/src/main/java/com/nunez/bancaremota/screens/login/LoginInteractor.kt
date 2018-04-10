package com.nunez.bancaremota.screens.login

import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class LoginInteractor(
        private val connectivityChecker: ConnectivityChecker,
        private val serviceProvider: ServiceProvider,
        private val androidScheduler: Scheduler,
        private val preferencesManager: PreferencesManager
) : LoginContract.Interactor {

    override fun requestAccessToken(username: String, passwor: String): Single<AccessTokenResponse> {
        val service = serviceProvider.getService()

        return if (connectivityChecker.isConected()) {
             service.getAccessToken(AccessTokenRequest(username, passwor))
                     .subscribeOn(Schedulers.io())
                     .observeOn(androidScheduler)
        } else {
            Single.error(NoConnectionException())
        }
    }

    override fun requestUserInfo(accessTokenResponse: AccessTokenResponse): Single<User> {
        val service = serviceProvider.getAuthorizedService()

        return if (connectivityChecker.isConected()) {
            service.getUserInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
        } else {
            Single.error(NoConnectionException())
        }
    }

    override fun saveUserInfo(user: User, accessToken: AccessTokenResponse): Completable {
        preferencesManager.saveUserInfo(user)
        return Completable.complete()
    }

    override fun saveToken(accessTokenResponse: AccessTokenResponse) {
        preferencesManager.saveAccessToken(accessTokenResponse)
    }
}