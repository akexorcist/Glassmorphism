package com.akexorcist.glassmorphism

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.RenderScript
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.drawToBitmap

class Glass2Layout : FrameLayout {
    private lateinit var renderScript: RenderScript

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
    }

    private fun setup() {
        renderScript = RenderScript.create(context)

    }

    fun getBlurBackground(): Bitmap {
        return rootView.drawToBitmap()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}