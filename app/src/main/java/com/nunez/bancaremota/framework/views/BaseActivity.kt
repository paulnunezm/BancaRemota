package com.nunez.palcine

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

abstract class BaseActivity : AppCompatActivity() {

    abstract var toolbarId: Int?
    abstract var layout: Int
    abstract var drawerId: Int?

    var drawerManager: DrawerManager? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = toolbarId?.let { this.findViewById(it) }
        toolbar?.let {
            setSupportActionBar(it)
        }

        drawerId?.let {
            val drawerLayout: DrawerLayout = this.findViewById(it)
            drawerManager = DrawerManager(drawerLayout, this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        drawerManager?.closeDrawerIfOpen()
    }
}