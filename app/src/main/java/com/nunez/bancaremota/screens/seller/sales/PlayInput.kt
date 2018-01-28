package com.nunez.bancaremota.screens.seller.sales

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.widget.EditText
import com.nunez.bancaremota.R
import com.nunez.palcine.framework.extensions.gone
import com.nunez.palcine.framework.extensions.show
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.input_play.view.*

class PlayInput @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var onGameEntered: PublishSubject<Game>

    companion object {
        private const val TAG = "PlayInput"
        private const val MAX_DIGITS = 2
    }

    init {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.input_play, this)

        onGameEntered = PublishSubject.create()
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

        amount.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                if (keyCode == KEYCODE_ENTER || keyCode == KEYCODE_DPAD_CENTER) {
                    onAmountEnterHandler()
                }
            }
            false
        }
    }

    fun requestInputFocus() {
        first.requestFocus()
    }

    private fun focusAmountInput() = amount.requestFocus()

    private fun setBehavior(editText: EditText, onMaxDigitPassed: (nextNumber: Char) -> Unit) {
        var maxDigitsReached = false

        editText.setOnKeyListener { _, keyCode, event ->
                when {
                    keyCode == KEYCODE_ENTER && event.action == ACTION_UP-> {
                        focusAmountInput()
                        return@setOnKeyListener true
                    }
                    enteredANumberAfterMaxDigitsPassed(event, maxDigitsReached) -> {
                        val number = event.number
                        onMaxDigitPassed(number)
                        return@setOnKeyListener false
                    }
                    isMaxDigitsBeenReached(editText, event.action) -> {
                        maxDigitsReached = true
                        return@setOnKeyListener true
                    }
                    else -> {
                        maxDigitsReached = false
                        return@setOnKeyListener false
                    }
                }
        }
    }

    private fun enteredANumberAfterMaxDigitsPassed(event: KeyEvent, reached: Boolean): Boolean {
        return event.action == ACTION_UP && reached && isKeyFromANumber(event)
    }

    private fun isMaxDigitsBeenReached(editText: EditText, action: Int): Boolean {
        return if(action == ACTION_DOWN){
            editText.text.toString().length == MAX_DIGITS
        }else {
            false
        }
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

    private fun onAmountEnterHandler() {
        val firstNumber = getNumberFromInput(first)
        val secondNumber = getNumberFromInput(second)
        val thirdNumber = getNumberFromInput(third)
        val amountPerPlay = getNumberFromInput(amount)

        if (areTheValuesCorrectForCreatingAGame(firstNumber, secondNumber, thirdNumber, amountPerPlay)) {
            val game = getCorrectGamePlay(firstNumber as Int, secondNumber, thirdNumber, amountPerPlay as Int)
            onGameEntered.onNext(game)
            resetFields()
        }
    }

    private fun areTheValuesCorrectForCreatingAGame(firstNumber: Int?, secondNumber: Int?, thirdNumber: Int?, amountPerPlay: Int?): Boolean {

        if (firstNumber == null) {
            first.error = "Can't be empty"
            return false
        }

        if (secondNumber == null && thirdNumber != null) {
            second.error = "Can't be empty"
            return false
        }

        if (amountPerPlay == null) {
            amount.error = "Can't be empty"
            return false
        }

        return true
    }

    private fun getCorrectGamePlay(firstNumber: Int, secondNumber: Int?, thirdNumber: Int?, amountPerPlay: Int): Game {
        return if (secondNumber == null && thirdNumber == null) {
            Quiniela(firstNumber, amountPerPlay, Game.LOTTERY_NOT_ASSIGNED)
        } else if (thirdNumber == null) {
            Pale(firstNumber, secondNumber as Int, amountPerPlay, Game.LOTTERY_NOT_ASSIGNED)
        } else {
            Tripleta(firstNumber, secondNumber as Int, thirdNumber, amountPerPlay, Game.LOTTERY_NOT_ASSIGNED)
        }
    }

    private fun resetFields() {
        first.apply {
            setText("")
            requestFocus()
        }
        second.setInitialPosition()
        third.setInitialPosition()
        amount.setText("")
    }

    private fun getNumberFromInput(editText: EditText): Int? {
        val enteredNumber = editText.text.toString()
        return if (enteredNumber.isEmpty()) {
            null
        } else {
            enteredNumber.toInt()
        }
    }

    private fun android.widget.EditText.showAndPutNextNumber(number: Char) {
        this.apply {
            show()
            setText(number.toString())
            setSelection(this.text.length)
            requestFocus()
        }
    }
    private fun android.widget.EditText.setInitialPosition() {
        this.apply {
            setText("")
            gone()
        }
    }

}