package com.nunez.bancaremota.screens.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import com.nunez.bancaremota.framework.helpers.ConnectivityChecker
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.framework.respository.BancappService
import com.nunez.bancaremota.framework.respository.ServiceProvider
import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import java.util.concurrent.TimeUnit

class LoginInteractorTest {
    val connectivityChecker: ConnectivityChecker = mock()
    val serviceProvider: ServiceProvider = mock()
    val service: BancappService = mock()
    val prefsManager : PreferencesManager = mock()
    val scheduler = TestScheduler()
    val interactor = LoginInteractor(connectivityChecker, serviceProvider, scheduler, prefsManager)

    val username = "peter"
    val password = "spiderman"
    val accessTokenRequest = AccessTokenRequest(username, password)
    val accessTokenResponse = AccessTokenResponse("Bearer", "sadf", "133", "1314")

    @Test
    fun requestAccessToken_withoutConnection_shouldReturnNoConnectionException() {
        // given
        whenever(connectivityChecker.isConected())
                .thenReturn(false)

        // when
        val testObserver = interactor.requestAccessToken(username, password).test()

        //  then
        testObserver.assertError(NoConnectionException::class.java)
    }

    @Test
    fun requestAccessToken_withConnection_shouldCallServiceMethod() {
        // given
        whenever(connectivityChecker.isConected())
                .thenReturn(true)

        whenever(service.getAccessToken(accessTokenRequest))
                .thenReturn(Single.just(accessTokenResponse))

        // when
        interactor.requestAccessToken(username, password).test()

        // then
        verify(service).getAccessToken(accessTokenRequest)
    }

    @Test
    fun requestAccessToken_tokenReceived_shouldReturnIt() {
        // given
        whenever(connectivityChecker.isConected())
                .thenReturn(true)
        whenever(service.getAccessToken(accessTokenRequest))
                .thenReturn(Single.just(accessTokenResponse))

        // when
        interactor.requestAccessToken(username, password).test()
                .assertNoErrors()
                .awaitDone(2, TimeUnit.SECONDS)
                .assertComplete()


    }

}