package com.nunez.bancaremota.screens.seller.sales

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.widget.EditText
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.framework.respository.data.Pale
import com.nunez.bancaremota.framework.respository.data.Quiniela
import com.nunez.bancaremota.framework.respository.data.Tripleta
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
                keyCode == KEYCODE_ENTER && event.action == ACTION_UP -> {
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
        return if (action == ACTION_DOWN) {
            editText.text.toString().length == MAX_DIGITS
        } else {
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
        val amountPerPlay = getNumberFromInput(amount)?.toFloat()

        if (areTheValuesCorrectForCreatingAGame(firstNumber, secondNumber, thirdNumber, amountPerPlay)) {
            val game = getCorrectGamePlay(firstNumber as Int, secondNumber, thirdNumber, amountPerPlay as Float)
            onGameEntered.onNext(game)
            resetFields()
        }
    }

    private fun areTheValuesCorrectForCreatingAGame(firstNumber: Int?, secondNumber: Int?, thirdNumber: Int?, amountPerPlay: Float?): Boolean {
        if (!checkIfNeededValuesAreSet(firstNumber, secondNumber, thirdNumber, amountPerPlay)) {
            return false
        } else if (areEqualNumbersOnInputs(firstNumber, secondNumber, thirdNumber)) {
            return false
        }
        return true
    }

    private fun checkIfNeededValuesAreSet(firstNumber: Int?, secondNumber: Int?, thirdNumber: Int?, amountPerPlay: Float?): Boolean {
        if (firstNumber == null) {
            setError(first, R.string.input_play_error_cant_be_empty)
            return false
        } else if (secondNumber == null && thirdNumber != null) {
            setError(second, R.string.input_play_error_cant_be_empty)
            return false
        } else if (amountPerPlay == null) {
            setError(amount, R.string.input_play_error_cant_be_empty)
            return false
        }

        return true
    }

    private fun areEqualNumbersOnInputs(firstNumber: Int?, secondNumber: Int?, thirdNumber: Int?): Boolean {

        if (thirdNumber != null) {
            if (firstNumber == thirdNumber) {
                third.error = context.getString(R.string.input_play_error_same_numbers, "Primero")
                return true
            } else {
                third.error = null
            }

            if (secondNumber == thirdNumber) {
                third.error = context.getString(R.string.input_play_error_same_numbers, "Segundo")
                return true
            } else {
                third.error = null
            }
        }

        if (secondNumber != null) {
            if (firstNumber == secondNumber) {
                second.error = context.getString(R.string.input_play_error_same_numbers, "Primero")
                return true
            } else {
                second.error = null
            }
        }

        return false
    }


    private fun setError(input: EditText, stringResourceId: Int) {
        input.error = getStringResource(stringResourceId)
    }

    private fun getStringResource(resourceId: Int): String {
        return context.getString(resourceId)
    }

    private fun getCorrectGamePlay(firstNumber: Int, secondNumber: Int?, thirdNumber: Int?, amountPerPlay: Float): Game {
        return if (secondNumber == null && thirdNumber == null) {
            Quiniela(firstNumber, amountPerPlay)
        } else if (thirdNumber == null) {
            Pale(firstNumber, secondNumber as Int, amountPerPlay)
        } else {
            Tripleta(firstNumber, secondNumber as Int, thirdNumber, amountPerPlay)
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