package com.nunez.palcine.framework.respository

import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.LotteryResponse
import com.nunez.bancaremota.framework.respository.data.User
import com.nunez.bancaremota.screens.seller.sales.SalesInteractor
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
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
    fun postPlays(@Body ticket: SalesInteractor.PostTicket): Single<Response<ResponseBody>>
}