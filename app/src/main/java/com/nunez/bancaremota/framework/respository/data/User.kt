package com.nunez.bancaremota.framework.respository.data

import com.squareup.moshi.Json

data class User(
        val name: String,
        val email: String,
        val status: String,
        @Json(name = "user_setting") val userSettings: UserSettings? = UserSettings()) {

    companion object {
        const val ENABLED = "enabled"
        const val DISABLED = "disabled"
    }
}

data class UserSettings(
        @Json(name = "q_first") val quinielaFirst: String? = "",
        @Json(name = "q_second") val quinielaSecond: String? = "",
        @Json(name = "q_third") val quinielaThird: String? = "",
        @Json(name = "p_first") val paleFirst: String? = "",
        @Json(name = "p_second") val paleSecond: String? = "",
        @Json(name = "super_p") val superPale: String? = "",
        @Json(name = "t_first") val tripletaFirst: String? = "",
        @Json(name = "t_second") val tripletaSecond: String? = ""
)