package com.nunez.bancaremota.screens.seller

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.View
import android.widget.TextView
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManagerImpl
import com.nunez.palcine.BaseActivity
import kotlinx.android.synthetic.main.activity_seller.*


class SellerActivity : BaseActivity() {
    override var toolbarId: Int? = R.id.toolbar
    override var layout: Int = R.layout.activity_seller
    override var drawerId: Int? = R.id.drawer_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val drawerNavigator = SellerDrawerNavitagor(
                this,
                R.id.contentFrame,
                supportFragmentManager,
                PreferencesManagerImpl(this),
                nav_view,
                drawerManager)

        drawerNavigator.navigateTo(R.id.sale)
        setSellerNameOnDrawer()
    }

    private fun setSellerNameOnDrawer() {
        val preferencesManager = PreferencesManagerImpl(this)
        val user = preferencesManager.retrieveUserInfo()
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val sellerName = headerView.findViewById(R.id.sellerName) as TextView
        sellerName.text = user.name
    }
}