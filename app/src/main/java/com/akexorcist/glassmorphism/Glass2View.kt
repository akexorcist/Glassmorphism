package com.akexorcist.glassmorphism

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.customview.view.AbsSavedState
import kotlin.math.abs

class Glass2View : FrameLayout {
    var cornerRadius: Float = 25f
    var padding: Float = 24f
    var surfaceOpacity: Float = 0.15f
    var borderOpacity: Float = 0.28f
    var shadowOpacity: Float = 0.19f
    var borderWidth: Float = 3f
    var blurAmount: Int = 25

    private val shapePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val surfacePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.WHITE
    }

    private val shadowPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.BLACK
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
    }

    constructor(context: Context) : super(context) {
        setupStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupStyleable(context, attrs)
    }

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Glass2View)
        this.cornerRadius = typedArray.getDimension(R.styleable.Glass2View_glass2View_cornerRadius, 25f)
        this.padding = typedArray.getDimension(R.styleable.Glass2View_glass2View_elevation, 24f)
        this.surfaceOpacity = typedArray.getFloat(R.styleable.Glass2View_glass2View_surfaceOpacity, 0.15f)
        this.borderOpacity = typedArray.getFloat(R.styleable.Glass2View_glass2View_borderWidth, 0.28f)
        this.shadowOpacity = typedArray.getFloat(R.styleable.Glass2View_glass2View_shadowOpacity, 0.19f)
        this.borderWidth = typedArray.getDimension(R.styleable.Glass2View_glass2View_borderWidth, 3f)
        this.blurAmount = typedArray.getInteger(R.styleable.Glass2View_glass2View_blurAmount, 25)
        typedArray.recycle()
        elevation = this.padding
        surfacePaint.alpha = (surfaceOpacity * 255).toInt()
        borderPaint.strokeWidth = borderWidth
        borderPaint.alpha = (borderOpacity * 255).toInt()
        shadowPaint.alpha = (shadowOpacity * 255).toInt()
        shadowPaint.maskFilter = BlurMaskFilter(
            padding,
            BlurMaskFilter.Blur.OUTER
        )
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState: Parcelable? = super.onSaveInstanceState()
        superState?.let {
            val state = SavedState(superState)
            state.cornerRadius = this.cornerRadius
            state.padding = this.padding
            state.surfaceOpacity = this.surfaceOpacity
            state.borderOpacity = this.borderOpacity
            state.shadowOpacity = this.shadowOpacity
            state.borderWidth = this.borderWidth
            state.blurAmount = this.blurAmount
            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                this.cornerRadius = state.cornerRadius
                this.padding = state.padding
                this.surfaceOpacity = state.surfaceOpacity
                this.borderOpacity = state.borderOpacity
                this.shadowOpacity = state.shadowOpacity
                this.borderWidth = state.borderWidth
                this.blurAmount = state.blurAmount
                elevation = this.padding
            }
            else -> {
                super.onRestoreInstanceState(state)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        with(canvas) {
            val left: Float = (borderWidth / 2f) + padding
            val top: Float = (borderWidth / 2f) + padding
            val right: Float = (width - (borderWidth / 2f)) - padding
            val bottom: Float = (height - (borderWidth / 2f)) - padding

            // Draw shadow
            drawRoundRect(
                left,
                top,
                right,
                bottom,
                cornerRadius,
                cornerRadius,
                shadowPaint
            )

            // Draw surface
            drawRoundRect(
                left,
                top,
                right,
                bottom,
                cornerRadius,
                cornerRadius,
                surfacePaint
            )

            // Draw border
            drawRoundRect(
                left,
                top,
                right,
                bottom,
                cornerRadius,
                cornerRadius,
                borderPaint
            )
        }
    }

    internal class SavedState : AbsSavedState {
        var cornerRadius: Float = 0f
        var padding: Float = 0f
        var surfaceOpacity: Float = 0f
        var borderOpacity: Float = 0f
        var shadowOpacity: Float = 0f
        var borderWidth: Float = 0f
        var blurAmount: Int = 0

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            cornerRadius = source.readFloat()
            padding = source.readFloat()
            surfaceOpacity = source.readFloat()
            borderOpacity = source.readFloat()
            shadowOpacity = source.readFloat()
            borderWidth = source.readFloat()
            blurAmount = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(cornerRadius)
            out.writeFloat(padding)
            out.writeFloat(surfaceOpacity)
            out.writeFloat(borderOpacity)
            out.writeFloat(shadowOpacity)
            out.writeFloat(borderWidth)
            out.writeInt(blurAmount)
        }

        companion object {
            @Suppress("unused")
            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> = object : Parcelable.ClassLoaderCreator<SavedState> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                    return SavedState(source, loader)
                }

                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source, null)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return newArray(size)
                }
            }
        }
    }
}