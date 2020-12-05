package com.akexorcist.glassmorphism

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class GlassBackgroundView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}