package com.nunez.palcine.framework.respository

import com.nunez.bancaremota.framework.respository.data.AccessTokenRequest
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BancappService {
    @POST("oauth/token")
    fun getAccessToken(@Body requestAccessToken: AccessTokenRequest): Single<AccessTokenResponse>

    @GET("/api/user")
    fun getUserInfo(): Single<User>
}