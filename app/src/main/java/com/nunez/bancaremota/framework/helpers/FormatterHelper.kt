package com.nunez.bancaremota.framework.helpers

class FormatterHelper {

    companion object {

        fun twoDigitsStringFormatter(number: Number): String {
            val n = number.toInt()
            return if (n < 10) {
                "0$n"
            } else {
                n.toString()
            }
        }

         fun getFormattedTicketNumber(ticketNumber: String): String {
            val numberGroup = 4
            var formattedTicketNumber = ""

            for (i in 0 until ticketNumber.length) {
                if (i != 0 && i % numberGroup == 0) {
                    formattedTicketNumber += "-"
                }
                formattedTicketNumber += ticketNumber[i]
            }
            return formattedTicketNumber
        }


    }

}