package com.dicoding.djoalanapplication.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.djoalanapplication.R

class EmailEditText: AppCompatEditText {

    private lateinit var symbol: Drawable
    var isCorrect: Boolean = false

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, atr: AttributeSet): super(context, atr){
        init()
    }
    constructor(context: Context, atr: AttributeSet, defStyleAtr: Int): super(context, atr, defStyleAtr) {
        init()
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(input: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (input.toString().isNotEmpty()) checkEmailFormat(input)
                if (isCorrect) showCheck() else showClose()
            }

            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }

        })
    }

    private fun checkEmailFormat(s: CharSequence) {
        isCorrect = s.contains('@', true)
    }

    private fun showCheck() {
        symbol = ContextCompat.getDrawable(context, R.drawable.check_24) as Drawable
        setSymbolDrawable(endText = symbol)
    }

    private fun showClose() {
        symbol = ContextCompat.getDrawable(context, R.drawable.close_24) as Drawable
        setSymbolDrawable(endText = symbol)
    }

    private fun setSymbolDrawable(
        startText: Drawable? = null,
        topText: Drawable? = null,
        bottomText: Drawable? = null,
        endText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startText,
            topText,
            endText,
            bottomText,
        )
    }

}