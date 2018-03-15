package com.nunez.bancaremota.framework.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.nunez.bancaremota.R
import com.nunez.palcine.framework.extensions.show

class MessageViewHandler(private val container: View) {

    private val context = container.context
    private val image = container.findViewById<ImageView>(R.id.messageIcon)
    private val message = container.findViewById<TextView>(R.id.messageText)

    fun showUnexpectedError() {
        container.show()
        message.text = context.getString(R.string.error_message_unexpected_error)
        image.setImageResource(R.drawable.ic_unexpected_error)
    }

    fun showNoConnectionError() {
        container.show()
        message.text = context.getString(R.string.error_message_no_connection)
        image.setImageResource(R.drawable.ic_no_connection)
    }

    fun showNoAvailableLotteriesError() {
        container.show()
        message.text = context.getString(R.string.error_message_no_available_lotteries)
        image.setImageResource(R.drawable.ic_no_lotteries)
    }

    fun showNoAvailablePlays() {
        container.show()
        message.text = context.getString(R.string.error_message_no_available_plays)
        image.setImageResource(R.drawable.ic_no_tickets_found)
    }

    fun showNoTicketsAvailable(){
        container.show()
        message.text = context.getString(R.string.error_no_available_tickets)
        image.setImageResource(R.drawable.ic_no_tickets_found)
    }
}