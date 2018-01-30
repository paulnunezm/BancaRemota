package com.nunez.bancaremota.screens.seller.sales.lotterySelector

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nunez.bancaremota.screens.seller.sales.Lottery
import com.nunez.palcine.framework.exceptions.NoConnectionException
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class LotterySelectorPresenterTest {

    val view: LotterySelectorContract.View = mock()
    val interactor: LotterySelectorContract.Interactor = mock()
    lateinit var presenter: LotterySelectorPresenter

    @Before
    fun setup() {
        presenter = LotterySelectorPresenter(view, interactor)
    }

    @Test
    fun getAvailableLotteries_always_shouldRequestAvailableLotteries() {
        // when
        presenter.getAvailableLotteries()

        // then
        verify(interactor).requestAvailableLotteries()
    }

    @Test
    fun getAvailableLotteries_always_shouldShowLoading() {
        // when
        presenter.getAvailableLotteries()

        // then
        verify(view).showLoading()
    }

    @Test
    fun requestAvailableLotteries_errorOccurred_shouldEmmitErrorAndHide() {
        // given
        val e = NoConnectionException()
        whenever(interactor.requestAvailableLotteries())
                .thenReturn(Single.error(e))

        // when
        presenter.getAvailableLotteries()

        // then
        verify(view).emmitError(e)
        verify(view).hide()
    }

    @Test
    fun getAvailableLotteries_emptyList_shouldHideAndEmmitError() {
        // given
        whenever(interactor.requestAvailableLotteries())
                .thenReturn(Single.just(emptyList()))

        // when
        presenter.getAvailableLotteries()

        // then
        verify(view).hide()
        verify(view).emmitNoAvailableLotteriesError()
    }

    @Test
    fun getAvailableLotteries_withData_shouldShowLotteries() {
        // given
        val lotteries = listOf(Lottery(1, "Leidsa"), Lottery(2, "Real"))

        whenever(interactor.requestAvailableLotteries())
                .thenReturn(Single.just(lotteries))

        // when
        presenter.getAvailableLotteries()

        // then
        verify(view).showLotteries(lotteries)
    }

}