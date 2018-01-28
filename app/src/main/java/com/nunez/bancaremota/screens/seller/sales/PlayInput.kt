package com.nunez.bancaremota.screens.seller.sales

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.nunez.bancaremota.R
import com.nunez.palcine.framework.extensions.show
import kotlinx.android.synthetic.main.input_play.view.*

class PlayInput @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_DIGITS = 2
    }

    init {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.input_play, this)

        setup()
    }

    private fun setup() {
        setBehavior(first, {
            second.showAndPutNextNumber(it)
        })
        setBehavior(second, {
            third.showAndPutNextNumber(it)
        })
        setBehavior(third, {
            amount.apply {
                setText(it.toString())
                setSelection(this.text.length)
                requestFocus()
            }
        })
    }

    private fun focusAmountInput() = amount.requestFocus()

    private fun setBehavior(editText: EditText, onMaxDigitPassed: (nextNumber: Char) -> Unit) {
        editText.setOnEditorActionListener(this::setOnEnterPressListener)

        var inputs = 0
        var reached = false


        editText.setOnKeyListener { v, keyCode, event ->
            when {
                event.keyCode == KEYCODE_ENTER -> {
                    focusAmountInput()
                    return@setOnKeyListener true
                }
                event.keyCode == KEYCODE_DEL -> {
                    reached = false
                    return@setOnKeyListener false
                }
                enteredANumberAfterMaxDigitsPassed(event, reached) -> {
                    val number = event.number
                    onMaxDigitPassed(number)
                    return@setOnKeyListener true
                }
                isMaxDigitsBeenReached(editText) -> {
                    reached = true
                    return@setOnKeyListener true
                }
                else -> return@setOnKeyListener false
            }
        }
    }


    private fun enteredANumberAfterMaxDigitsPassed(event: KeyEvent, reached: Boolean): Boolean {
        return event.action == KeyEvent.ACTION_UP && reached && isKeyFromANumber(event)
    }

    private fun isMaxDigitsBeenReached(editText: EditText): Boolean {
        return editText.text.toString().length == MAX_DIGITS
    }

    private fun isKeyFromANumber(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KEYCODE_0,
            KEYCODE_1,
            KEYCODE_2,
            KEYCODE_3,
            KEYCODE_4,
            KEYCODE_5,
            KEYCODE_6,
            KEYCODE_7,
            KEYCODE_8,
            KEYCODE_9 -> {
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun setOnEnterPressListener(textView: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KEYCODE_ENTER) {
            focusAmountInput()
        }
        return false
    }

    private fun android.widget.EditText.showAndPutNextNumber(number: Char) {
        this.apply {
            show()
            setText(number.toString())
            setSelection(this.text.length)
            requestFocus()
        }
    }

}