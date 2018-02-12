package com.nunez.bancaremota.screens.seller

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.bancaremota.screens.login.LoginActivity
import com.nunez.bancaremota.screens.seller.sales.SalesFragment
import com.nunez.bancaremota.screens.seller.stats.StatsFragment
import com.nunez.palcine.DrawerDelegate
import com.nunez.palcine.DrawerNavigator

class SellerDrawerNavitagor(
        private val activity: AppCompatActivity,
        private val contentFrameId: Int,
        private val fragmentManager: FragmentManager,
        private val prefManager: PreferencesManager,
        drawerNavigationView: NavigationView,
        drawerManager: DrawerDelegate?
) : DrawerNavigator(drawerNavigationView, drawerManager) {
    override fun onItemSelected(item: MenuItem) {
        navigateTo(item.itemId)
    }

    fun navigateTo(itemId: Int) {
        when (itemId) {
            R.id.sale -> {
                replaceFragmentTo(SalesFragment.newInstance())
            }
            R.id.stats -> {
                replaceFragmentTo(StatsFragment.newInstance())
            }
            R.id.logout -> {
                prefManager.logout()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
                activity.finish()
            }
            else -> {
                throw IllegalStateException("item not specified on the activity_home_drawer_menu")
            }
        }

    }

    private fun replaceFragmentTo(f: Fragment) {
        fragmentManager.beginTransaction()
                .replace(contentFrameId, f)
                .commit()
    }

}