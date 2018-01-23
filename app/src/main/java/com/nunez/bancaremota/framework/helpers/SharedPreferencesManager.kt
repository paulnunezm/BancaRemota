package com.nunez.bancaremota.framework.helpers

import android.content.Context
import android.content.SharedPreferences
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.respository.data.AccessTokenResponse
import com.nunez.bancaremota.framework.respository.data.User


class PreferencesManagerImpl(
        private val context: Context
) : PreferencesManager {

    private val preference: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.user_prefs), 0)

    override fun saveAccessToken(accessTokenResponse: AccessTokenResponse) {
        val editor = preference.edit()
        editor.putString("access_token", accessTokenResponse.tokenType + " " + accessTokenResponse.accessToken)
        editor.apply()

    }

    override fun saveUserInfo(user: User) {
        val edior = preference.edit()
        edior.apply {
            putString("user_name", user.name)
            putString("user_email", user.email)
            putString("user_status", user.status)
        }
        edior.apply()
    }

    override fun retrieveAccessToken(): String {
        return preference.getString("access_token", "")
    }

    override fun retrieveUserInfo(): User {
        val name = preference.getString("user_name", "")
        val email = preference.getString("user_email", "")
        val status = preference.getString("user_status", "disabled")
        return User(name, email, status)
    }

    private fun getStringRes(resource: Int): String = context.getString(resource)

}

interface PreferencesManager {
    fun saveAccessToken(accessTokenResponse: AccessTokenResponse)
    fun saveUserInfo(user: User)
    fun retrieveAccessToken(): String
    fun retrieveUserInfo(): User
}