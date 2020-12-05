package com.akexorcist.glassmorphism

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.customview.view.AbsSavedState

class GlassView : FrameLayout {
    var cornerRadius: Float = DEFAULT_CORNER_RADIUS
        set(value) {
            field = value
            invalidate()
        }
    var padding: Float = DEFAULT_PADDING
        set(value) {
            field = value
            invalidate()
        }
    var shadowOpacity: Float = DEFAULT_SHADOW_OPACITY
        set(value) {
            field = value
            invalidate()
        }
    var surfaceOpacity: Float = DEFAULT_SURFACE_OPACITY
        set(value) {
            field = value
            invalidate()
        }
    var borderOpacity: Float = DEFAULT_BORDER_OPACITY
        set(value) {
            field = value
            invalidate()
        }
    var borderWidth: Float = DEFAULT_BORDER_WIDTH
        set(value) {
            field = value
            invalidate()
        }

    @Suppress("MayBeConstant")
    companion object {
        val DEFAULT_CORNER_RADIUS = 25f
        val DEFAULT_PADDING = 24f
        val DEFAULT_SHADOW_OPACITY = 0.19f
        val DEFAULT_SURFACE_OPACITY = 0.15f
        val DEFAULT_BORDER_OPACITY = 0.28f
        val DEFAULT_BORDER_WIDTH = 3f
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlassView)
        this.cornerRadius = typedArray.getDimension(R.styleable.GlassView_glassView_cornerRadius, DEFAULT_CORNER_RADIUS)
        this.padding = typedArray.getDimension(R.styleable.GlassView_glassView_elevation, DEFAULT_PADDING)
        this.surfaceOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_surfaceOpacity, DEFAULT_SURFACE_OPACITY)
        this.borderOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_borderWidth, DEFAULT_BORDER_OPACITY)
        this.shadowOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_shadowOpacity, DEFAULT_SHADOW_OPACITY)
        this.borderWidth = typedArray.getDimension(R.styleable.GlassView_glassView_borderWidth, DEFAULT_BORDER_WIDTH)
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

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            cornerRadius = source.readFloat()
            padding = source.readFloat()
            surfaceOpacity = source.readFloat()
            borderOpacity = source.readFloat()
            shadowOpacity = source.readFloat()
            borderWidth = source.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(cornerRadius)
            out.writeFloat(padding)
            out.writeFloat(surfaceOpacity)
            out.writeFloat(borderOpacity)
            out.writeFloat(shadowOpacity)
            out.writeFloat(borderWidth)
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