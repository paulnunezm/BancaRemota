package com.nunez.palcine.framework.extensions

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG){
    Snackbar.make(this, message, duration).show()
}

fun EditText.setText(text: String, bufferType: TextView.BufferType = TextView.BufferType.EDITABLE){
    this.setText(text, bufferType)
}