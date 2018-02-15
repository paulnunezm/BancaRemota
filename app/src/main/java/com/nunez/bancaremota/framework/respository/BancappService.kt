package com.nunez.palcine.framework.respository

import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.LotteryResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.PlayAvailabilityResponse
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.TicketBriefInteractor
import com.nunez.bancaremota.screens.seller.winningNumbers.WinningNumbersResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BancappService {
    @POST(Endpoints.REQUEST_ACCESS_TOKEN)
    fun getAccessToken(@Body requestAccessToken: AccessTokenRequest): Single<AccessTokenResponse>

    @GET(Endpoints.USER_INFO)
    fun getUserInfo(): Single<User>

    @GET(Endpoints.GET_AVAILABLE_LOTTERIES)
    fun getAvailableLotteriesToSell(): Single<LotteryResponse>

    @POST(Endpoints.POST_PLAYS)
    fun postPlays(@Body ticket: TicketBriefInteractor.PostTicket): Single<PlayAvailabilityResponse>

    @GET(Endpoints.WINNING_NUMBERS)
    fun getWinningNumbers(): Single<WinningNumbersResponse>
}