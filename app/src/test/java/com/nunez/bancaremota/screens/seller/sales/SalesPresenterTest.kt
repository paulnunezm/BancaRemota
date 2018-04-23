package com.nunez.bancaremota.screens.seller.sales

import com.nhaarman.mockito_kotlin.*
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.framework.respository.data.Lottery
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SalesPresenterTest {

    private val game = Game(1, 14, 24, 50, 1, "Leidsa", 2)
    private val lottery = Lottery(1, "Leidsa")

    private lateinit var view: SalesContract.View
    private lateinit var presenter: SalesPresenter

    @Test
    fun observeGameEntry_onGameEntered_showLotterySelector() {
        setViewWithHappyPath()
        presenter.observeGameEntry()

        // then
        verify(view).showLotteriesSelector()
    }

    @Test
    fun sellButtonPressed_showLoadingAndPostPlays() {
        setViewWithHappyPath()

        // when
        presenter.onSellButtonPressed()

        // then
        verify(view).showLoading()
    }

    @Test
    fun onLotteryEntry_lotteriesSelected_addPlaysToView() {
        setViewWithHappyPath()
        presenter.observeGameEntry()
        presenter.observeLotteryEntry()

        // when
        verify(view).addPlay(any())
    }

    @Test
    fun onGamEntry_unexpectedError_showUnexpectedError() {
        setViewWithUnexpectedErrors()
        presenter.observeGameEntry()

        verify(view).showUnexpectedError()
    }

    @Test
    fun onLotterySelected_shouldShowProcessOrderButton(){
        setViewWithHappyPath()

        presenter.observeGameEntry()
        presenter.observeLotteryEntry()

        verify(view).showProcessOrderButton()
    }

    @Test
    fun onPlayDeleted_ifNoMorePlaysAfterDeletion_shouldHideTheProcessOrderButton(){
        // given
        setViewWithHappyPath()
        presenter.observeGameEntry()
        presenter.observeLotteryEntry()

        // when
        presenter.onPlayDeleted(0)

        // then
        verify(view).hideProcessOrderButton()
    }

    @Test
    fun onPlayDeleted_ifMorePlaysAfterDeletion_shouldNotHideTheProcessOrderButton(){
        // given
        setViewWithHappyPath()
        presenter.observeGameEntry()
        presenter.observeLotteryEntry()
        presenter.plays = arrayListOf(game, game)

        // when
        presenter.onPlayDeleted(0)

        // then
        verify(view, never()).hideProcessOrderButton()
    }

    private fun setViewWithUnexpectedErrors(){
        view = mock<SalesContract.View> {
            on { observeGameEntry() } doReturn Observable.error(Throwable())
            on { observeSelectedLotteries() } doReturn Observable.error(Throwable())
        }
        presenter = SalesPresenter(view)
    }


    private fun setViewWithHappyPath() {
        view = mock()
        whenever(view.observeGameEntry())
                .thenReturn(Observable.just(game))
        whenever(view.observeSelectedLotteries())
                .thenReturn(Observable.just(lottery))

        presenter = SalesPresenter(view)
    }
}
