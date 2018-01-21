package com.nunez.palcine.framework.extensions

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadFromUrl(url: String) {
    Picasso.with(this.context)
            .load(url)
            .fit()
            .centerCrop()
            .into(this)
}