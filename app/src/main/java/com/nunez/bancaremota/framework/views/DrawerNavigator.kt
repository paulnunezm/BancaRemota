package com.nunez.palcine

import android.support.design.widget.NavigationView
import android.view.MenuItem

abstract class DrawerNavigator(private val drawerNavigationView: NavigationView,
                               private val drawerManager: DrawerDelegate?
) : NavigationView.OnNavigationItemSelectedListener {

    init {
        setNavigationListener()
    }

    private fun setNavigationListener(){
        drawerNavigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        onItemSelected(item)
        drawerManager?.closeDrawerIfOpen()
        return true
    }

    abstract fun onItemSelected(item: MenuItem)
}