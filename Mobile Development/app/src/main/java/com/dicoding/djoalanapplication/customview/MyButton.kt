package com.dicoding.djoalanapplication.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.djoalanapplication.R

class MyButton: AppCompatButton {

    private lateinit var disable: Drawable
    private lateinit var enable: Drawable
    private var txtColor: Int = 0

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, R.color.white)
        disable = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
        enable = ContextCompat.getDrawable(context, R.drawable.bg_button_enable) as Drawable
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) enable else disable
        setTextColor(txtColor)
    }
}