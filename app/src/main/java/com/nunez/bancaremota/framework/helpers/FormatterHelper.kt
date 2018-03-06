package com.nunez.bancaremota.framework.helpers

class FormatterHelper {

    companion object {

        fun twoDigitsStringFormatter(number: Number): String {
            val n = number.toInt()
            return if (n < 10){
                "0$n"
            }else{
                n.toString()
            }
        }

    }

}