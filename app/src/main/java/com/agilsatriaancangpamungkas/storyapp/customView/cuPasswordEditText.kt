package com.agilsatriaancangpamungkas.storyapp.customView

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.agilsatriaancangpamungkas.storyapp.R

class cuPasswordEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(cs: CharSequence?, start: Int, lengthbefore: Int, lengthafter: Int) {

            }

            override fun onTextChanged(cs: CharSequence?, start: Int, lengthbefore: Int, lengthafter: Int) {
                if (cs.toString().length < 8) {
                    setError(resources.getString(R.string.password_incorrect), null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(cs: Editable?) {

            }
        })
    }
}