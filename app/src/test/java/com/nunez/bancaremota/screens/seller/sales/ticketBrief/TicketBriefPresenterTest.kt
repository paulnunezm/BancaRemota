package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import com.nhaarman.mockito_kotlin.*
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.framework.respository.data.Pale
import com.nunez.bancaremota.framework.respository.data.Quiniela
import com.nunez.palcine.framework.exceptions.NoConnectionException
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Test

class TicketBriefPresenterTest {
    val view: TicketBriefContract.View = mock()
    val interactor: TicketBriefContract.Interactor = mock()
    val presenter = TicketBriefPresenter(view, interactor)

    val plays = arrayListOf(
            Quiniela(1, 20),
            Pale(1, 14, 20),
            Quiniela(1, 20),
            Pale(1, 14, 20),
            Quiniela(1, 20),
            Pale(1, 14, 20)
    )

    val playAvailabilityResponse = PlayAvailabilityResponses()

    @Test
    fun getPlaysAvailability_always_showLoading() {
        //when
        presenter.getPlaysAvailability(plays)

        //then
        verify(view).showLoading()
    }

    @Test
    fun getPlaysAvailability_allPlaysOk_showAvailablePlays() {
        // given
        whenever(interactor.checkPlaysAvailability(plays))
                .thenReturn(Single.just(playAvailabilityResponse.happyPathPlayAvailability()))

        // when
        presenter.getPlaysAvailability(plays)

        // then
        var argumentCaptor = argumentCaptor<List<Game>>()
        verify(view).hideLoading()
        verify(view).showAvailablePlays(argumentCaptor.capture())
        assertEquals(plays.size, argumentCaptor.firstValue.size)
    }

    @Test
    fun getPlaysAvailability_allPlaysOk_shouldNeverShowUnavailablePlaysDialog() {
        // given
        whenever(interactor.checkPlaysAvailability(plays))
                .thenReturn(Single.just(playAvailabilityResponse.happyPathPlayAvailability()))

        // when
        presenter.getPlaysAvailability(plays)

        // then
        verify(view, never()).showUnavailablePlaysDialog(any())
    }

    @Test
    fun getPlaysAvailability_somePlaysOk_showDialogWithUnavailablePlays() {
        // given
        whenever(interactor.checkPlaysAvailability(plays))
                .thenReturn(Single.just(playAvailabilityResponse
                        .unhappyPath_unnavailableGames))

        // when
        presenter.getPlaysAvailability(plays)

        // then
        var captor = argumentCaptor<List<Game>>()
        verify(view).showUnavailablePlaysDialog(captor.capture())
        assertEquals(
                playAvailabilityResponse.availableOnesCount,
                captor.firstValue.size)
    }

    @Test
    fun getPlaysAvailability_unsuccessfulResponse_shouldShowUnexpectedError() {
        // given
        whenever(interactor.checkPlaysAvailability(plays))
                .thenReturn(Single.just(playAvailabilityResponse
                        .unsuccessfulResponse))

        // when
        presenter.getPlaysAvailability(plays)

        // then
        verify(view).hideLoading()
        verify(view).showUnexpectedError()
        verify(view, never()).showUnavailablePlaysDialog(any())
        verify(view, never()).showAvailablePlays(any())
    }

    @Test
    fun getPlaysAvailability_noConnectivity_shouldShowConnectivityError(){
        whenever(interactor.checkPlaysAvailability(plays))
                .thenReturn(Single.error(NoConnectionException()))

        presenter.getPlaysAvailability(plays)

        verify(view).hideLoading()
        verify(view).showConnectivityError()
    }


}

class PlayAvailabilityResponses {

    fun happyPathPlayAvailability(): PlayAvailabilityResponse {
        val playAvaibility = arrayListOf( // Same length as the plays
                PlayAvailability(0, 0),
                PlayAvailability(1, 0),
                PlayAvailability(2, 0),
                PlayAvailability(3, 0),
                PlayAvailability(4, 0),
                PlayAvailability(5, 0)
        )
        return PlayAvailabilityResponse(true, playAvaibility)
    }


    val availableOnesCount = 3
    val unhappyPath_unnavailableGames: PlayAvailabilityResponse
        get() {
            val playAvaibility = arrayListOf( // Same length as the plays
                    PlayAvailability(0, PlayAvailability.CODE_NO_ERROR),
                    PlayAvailability(1, PlayAvailability.CODE_AMOUNT_EXCEEDED),
                    PlayAvailability(2, PlayAvailability.CODE_LIMIT_EXCEEDED),
                    PlayAvailability(3, PlayAvailability.CODE_NO_ERROR),
                    PlayAvailability(4, PlayAvailability.CODE_SCHEDULE_ERROR),
                    PlayAvailability(5, PlayAvailability.CODE_NO_ERROR)
            )
            return PlayAvailabilityResponse(true, playAvaibility)
        }

    val unsuccessfulResponse: PlayAvailabilityResponse
        get() = PlayAvailabilityResponse(false, emptyList())
}