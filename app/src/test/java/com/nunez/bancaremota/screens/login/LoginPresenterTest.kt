package com.nunez.bancaremota.screens.login

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.framework.exceptions.NoConnectionException
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginPresenterTest {

    val interactor: LoginContract.Interactor = mock()
    val view: LoginContract.View = mock()
    lateinit var presenter: LoginPresenter
    lateinit var interactorRobot: InteractorRobot

    val username = "Peter"
    val password = "Spiderman"
    val accessTokenResponse = AccessTokenResponse("Bearer", "asdf234", "sar34", "425")
    val user = User(username,
            username + "@gmail.com",
            "enabled")

    @Before
    fun setUp() {
        presenter = LoginPresenter(view, interactor)
        interactorRobot = InteractorRobot(interactor, username, password, user, accessTokenResponse)
    }

    @Test
    fun loginButtonPressed_shouldRequestAccessToken() {
        //Given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()
                .setRequestUserInfo_returnsInfo_userEnabled()

        // When
        presenter.onLoginButtonPressed(username, password)

        // then
        verify(interactor).requestAccessToken(username, password)
    }

    @Test
    fun loginButtonPressed_shouldShowLoading() {
        //Given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()

        // When
        loginButtonPressed()

        // then
        verify(view).showLoading()
    }

    @Test
    fun loginButtonPressed_correctCredentials_shouldRequestUserInfo() {
        // Given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()

        // when
        presenter.onLoginButtonPressed(username, password)

        // then
        verify(interactor).requestUserInfo(accessTokenResponse)
    }

    @Test
    fun loginButtonPressed_correctCredentials_shouldSaveToken() {
        // Given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()

        // when
        presenter.onLoginButtonPressed(username, password)

        // then
        verify(interactor).saveToken(accessTokenResponse)
    }

    @Test
    fun loginButtonPressed_incorrectCredentials_shouldShowLoginError() {
        // Given
        interactorRobot.setRequestAccessToken_returnsException()

        // when
        loginButtonPressed()

        // Then
        verify(view).showLoginError()
    }


    @Test
    fun loginButtonPressed_requestingAccessTokenWithoutConnection_shouldShowConnectionErrorAndHideLogin() {
        // Given
        interactorRobot.setRequestAccessToken_returnsNoConnectionException()

        // when
        loginButtonPressed()

        // Then
        verify(view).showNoConnectionError()
        verify(view).hideLoading()
    }

    @Test
    fun loginButtonPressed_requestingUserInfoWithoutConnection_shouldShowConnectionError() {
        //given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()
                .setRequestUserInfo_returnsNoConnectionException()

        // when
        loginButtonPressed()

        /// then
        verify(view).hideLoading()
        verify(view).showNoConnectionError()
    }

    @Test
    fun requestUserInfo_statusDisabledInfo_shouldShowDisabledMessageAndHideLogin() {
        // given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()
                .setRequestUserInfo_returnsInfo_userDisabled()

        // when
        loginButtonPressed()

        // then
        verify(view).showUserBlockedError()
        verify(view).hideLoading()
    }

    @Test
    fun onUserInfoResponse_saveCompletes_shouldSaveItAndGoToSellerActivity() {
        // given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()
                .setRequestUserInfo_returnsInfo_userEnabled()
                .saveInfo_completes()

        // when
        loginButtonPressed()

        // then
        verify(interactor).saveUserInfo(user, accessTokenResponse)
        verify(view).goToSellerActivity()
    }


    @Test
    fun onUserInfoResponse_saveFails_shouldShowUnexpectedErrorAndHideLogin() {
        // given
        interactorRobot.setRequestAccessToken_returnsAccessTokenResponse()
                .setRequestUserInfo_returnsInfo_userEnabled()
                .saveInfo_error()

        // when
        loginButtonPressed()

        // then
        verify(view).hideLoading()
        verify(view).showUnexpectedError()
    }


    private fun loginButtonPressed() {
        presenter.onLoginButtonPressed(username, password)
    }

    class InteractorRobot(
            private val interactor: LoginContract.Interactor,
            private val username: String,
            private val password: String,
            private val user: User,
            private val accessTokenResponse: AccessTokenResponse) {

        fun setRequestAccessToken_returnsAccessTokenResponse(): InteractorRobot {
            whenever(interactor.requestAccessToken(username, password))
                    .thenReturn(Single.just(accessTokenResponse))
            return this
        }

        fun setRequestUserInfo_returnsInfo_userEnabled(): InteractorRobot {

            whenever(interactor.requestUserInfo(accessTokenResponse))
                    .thenReturn(Single.just(user))
            return this
        }

        fun setRequestUserInfo_returnsNoConnectionException(): InteractorRobot {
            whenever(requestUserInfo())
                    .thenReturn(Single.error(NoConnectionException()))
            return this
        }

        fun setRequestAccessToken_returnsException(): InteractorRobot {
            whenever(interactor.requestAccessToken(username, password))
                    .thenReturn(Single.error(Throwable()))
            return this

        }

        fun setRequestAccessToken_returnsNoConnectionException(): InteractorRobot {
            whenever(requestAccessToken())
                    .thenReturn(Single.error(NoConnectionException()))
            return this
        }

        fun setRequestUserInfo_returnsInfo_userDisabled(): InteractorRobot {
            val user = User(username,
                    username + "@gmail.com",
                    "disabled")

            whenever(requestUserInfo())
                    .thenReturn(Single.just(user))
            return this
        }

        fun saveInfo_completes(): InteractorRobot {
            whenever(interactor.saveUserInfo(user, accessTokenResponse))
                    .thenReturn(Completable.complete())
            return this
        }

        fun saveInfo_error(): InteractorRobot {
            whenever(interactor.saveUserInfo(user, accessTokenResponse))
                    .thenReturn(Completable.error(Throwable()))
            return this
        }

        private fun requestAccessToken(): Single<AccessTokenResponse> {
            return interactor.requestAccessToken(username, password)
        }

        private fun requestUserInfo(): Single<User> {
            return interactor.requestUserInfo(accessTokenResponse)
        }
    }
}

