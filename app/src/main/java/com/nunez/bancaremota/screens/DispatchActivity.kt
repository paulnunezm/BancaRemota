package com.nunez.bancaremota.screens

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.bancaremota.screens.login.LoginActivity
import com.nunez.bancaremota.screens.seller.SellerActivity

class DispatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesManager = PreferencesManagerImpl(this)
        val user = preferencesManager.retrieveUserInfo()

        if (user.name.isEmpty() && user.email.isEmpty()) {
            goToLoginActivity()
        } else {
            goToSellerActivity()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        goToActivity(intent)
    }

    private fun goToSellerActivity() {
        val intent = Intent(this, SellerActivity::class.java)
        goToActivity(intent)
    }

    private fun goToActivity(i: Intent) {
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
        finish()
    }
}