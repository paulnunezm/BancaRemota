package com.nunez.bancaremota.framework.views

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.nunez.bancaremota.R

class MessageHandler(
        val context: Context,
        val imageView: ImageView,
        val textView: TextView) {

    fun setUnexpectedErrorMessage() {
        textView.text = getString(R.string.error_message_unexpected_error)
    }

    fun setNoConnectionError() {
        textView.text = getString(R.string.error_message_no_connection)
    }

    fun setCustomMessage(stringRes:Int, imageRes: Int){
        textView.text = getString(stringRes)
    }


    private fun getString(id: Int): String {
        return context.getString(id)
    }
}