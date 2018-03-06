package com.nunez.palcine.framework.respository

import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.LotteryResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.PlayAvailabilityResponse
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.PrintResponse
import com.nunez.bancaremota.screens.seller.sales.ticketBrief.TicketBriefInteractor
import com.nunez.bancaremota.screens.seller.tickets.TicketsResponse
import com.nunez.bancaremota.screens.seller.tickets.payTicketBottomSheet.PayResponse
import com.nunez.bancaremota.screens.seller.winningNumbers.WinningNumbersResponse
import io.reactivex.Single
import retrofit2.http.*

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

    @GET(Endpoints.TICKETS)
    fun getTodayTickets(): Single<TicketsResponse>

    @POST(Endpoints.SEARCH_TICKET)
    @FormUrlEncoded
    fun searchTicket(@Field("ticket_number") ticketNumber: String): Single<TicketsResponse>

    @POST(Endpoints.PAY)
    @FormUrlEncoded
    fun payTicket(@Field("ticket_id") id: String): Single<PayResponse>

    @POST(Endpoints.PRINT)
    @FormUrlEncoded
    fun printTicket(@Field("ticket_id") id: String): Single<PrintResponse>
}