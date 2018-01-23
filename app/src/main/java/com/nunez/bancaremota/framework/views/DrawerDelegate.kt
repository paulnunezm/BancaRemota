package com.nunez.palcine

import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import com.nunez.bancaremota.R


abstract class DrawerDelegate(
        private val drawerLayout: DrawerLayout,
        private val activity: BaseActivity) {

    init {
        activity.toolbar?.let { enableToolbarToggle() }
    }

    private fun enableToolbarToggle() {
        val toggle = ActionBarDrawerToggle(
                activity, drawerLayout, activity.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun closeDrawerIfOpen(): Boolean {
        return if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        } else {
            false
        }
    }
}

class DrawerManager(
        drawerLayout: DrawerLayout,
        activity: BaseActivity
) : DrawerDelegate(drawerLayout, activity)