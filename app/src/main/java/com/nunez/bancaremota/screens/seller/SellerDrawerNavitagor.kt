package com.nunez.bancaremota.screens.seller

import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import com.nunez.bancaremota.R
import com.nunez.palcine.DrawerDelegate
import com.nunez.palcine.DrawerNavigator

class SellerDrawerNavitagor(
        private val contentFrameId: Int,
        private val fragmentManager: FragmentManager,
        drawerNavigationView: NavigationView,
        drawerManager: DrawerDelegate?
) : DrawerNavigator(drawerNavigationView, drawerManager) {
    override fun onItemSelected(item: MenuItem) {
        navigateTo(item.itemId)
    }

    fun navigateTo(itemId: Int) {
        val fragment = when (itemId) {
            R.id.sale -> {
                Fragment()
            }
            else -> {
                throw IllegalStateException("item not specified on the activity_home_drawer_menu")
            }
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(contentFrameId, fragment)
                .commit()
    }

}