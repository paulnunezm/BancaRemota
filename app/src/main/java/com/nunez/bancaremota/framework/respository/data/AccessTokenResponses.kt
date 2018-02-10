package com.nunez.bancaremota.framework.respository.data

import com.squareup.moshi.Json

data class AccessTokenResponse(
        @Json(name = "token_type") val tokenType: String,
        @Json(name = "access_token") val accessToken: String,
        @Json(name = "refresh_token") val refreshToken: String,
        @Json(name = "expires_in") val expiresIn: String)

data class AccessTokenRequest(
        val username: String,
        val password: String) {

    val grant_type: String = "password"
    val client_id: String = "2"
    val client_secret: String = "9lAb4VcE1LKOzUagdaTgMDOEGSQx362nfkbFIGzw"
}