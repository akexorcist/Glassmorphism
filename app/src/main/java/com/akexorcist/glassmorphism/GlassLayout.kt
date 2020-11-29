package com.akexorcist.glassmorphism

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Parcel
import android.os.Parcelable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.view.AbsSavedState
import java.lang.RuntimeException
import kotlin.math.abs

class GlassLayout : FrameLayout {
    private lateinit var renderScript: RenderScript
    private val rootRect = Rect()
    private var blurSampleSize = 2f

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private val shapePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val surfacePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private val blurPaint = Paint().apply {
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
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
        setup()
        setupStyleable(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup()
        setupStyleable(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
        setupStyleable(context, attrs)
    }

    private fun setup() {
        renderScript = RenderScript.create(context)
        if (childCount > 1 ||
            (childCount > 0 && getChildAt(0) !is ViewGroup)
        ) {
            throw RuntimeException("Required only 1 view group inside GlassLayout")
        }
    }

    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlassLayout)
        this.blurSampleSize = typedArray.getFloat(R.styleable.GlassLayout_glassView_blurSampleSize, 2f)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        if (childCount > 0) {
            getGlobalVisibleRect(rootRect)
            (getChildAt(0) as? ViewGroup)?.let { viewGroup ->
                viewGroup.children.iterator().forEach { view ->
                    if (view is GlassView) {
                        updateGlassView(view, viewGroup, rootRect)
                    }
                }
            }
        }
    }

    private fun getViewAsBitmap(view: View, left: Int, top: Int, width: Int, height: Int): Bitmap {
        val bitmap = view.toBitmap(
            sampleSize = blurSampleSize,
            clipRect = Rect(
                left,
                top,
                left + width,
                top + height
            ),
            config = Bitmap.Config.ARGB_8888
        )
        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            (left / blurSampleSize).toInt(),
            (top / blurSampleSize).toInt(),
            (width / blurSampleSize).toInt(),
            (height / blurSampleSize).toInt()
        )
        bitmap.recycle()
        return croppedBitmap
    }

    var onBitmapUpdated: ((Bitmap) -> Unit)? = null

    private fun blur(bitmap: Bitmap, blurAmount: Int) {
        if (blurAmount == 0) return
        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val output = Allocation.createTyped(renderScript, input.type)
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript)).apply {
            setRadius(blurAmount.toFloat())
            setInput(input)
            forEach(output)
        }
        output.copyTo(bitmap)
    }

    private fun updateGlassView(view: GlassView, rootView: ViewGroup, rootRect: Rect) {
        val startTime = System.currentTimeMillis()
        val visibleRect = Rect()
        view.getGlobalVisibleRect(visibleRect)
        view.visibility = View.INVISIBLE
        val bitmap = getViewAsBitmap(
            rootView,
            visibleRect.left - rootRect.left,
            visibleRect.top - rootRect.top,
            visibleRect.width(),
            visibleRect.height()
        )
        blur(bitmap, view.blurAmount)
        val glassBitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val padding = view.padding
        val borderOffset = view.borderWidth
        val cornerRadius = view.cornerRadius
        surfacePaint.alpha = (view.surfaceOpacity * 255).toInt()
        borderPaint.strokeWidth = borderOffset
        borderPaint.alpha = (view.borderOpacity * 255).toInt()
        shadowPaint.alpha = (view.shadowOpacity * 255).toInt()
        shadowPaint.maskFilter = BlurMaskFilter(
            padding,
            BlurMaskFilter.Blur.OUTER
        )
        Canvas(glassBitmap).apply {
            val left: Float = (borderOffset / 2f) + padding
            val top: Float = (borderOffset / 2f) + padding
            val right: Float = (width - (borderOffset / 2f)) - padding
            val bottom: Float = (height - (borderOffset / 2f)) - padding
            val leftOffset: Float = getDrawingOffset(
                visibleRect.left == 0,
                width,
                (visibleRect.right - visibleRect.left)
            )
            val topOffset: Float = getDrawingOffset(
                rootRect.top - visibleRect.top == 0,
                height,
                (visibleRect.bottom - visibleRect.top)
            )

            // Draw shape with round corner
            drawRoundRect(
                left,
                top,
                right,
                bottom,
                cornerRadius,
                cornerRadius,
                shapePaint
            )

            // Draw blur background
            drawBitmap(
                bitmap,
                Matrix().apply {
                    postScale(blurSampleSize, blurSampleSize)
                    postTranslate(leftOffset, topOffset)
                },
                blurPaint
            )

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
        bitmap.recycle()
        view.visibility = View.VISIBLE
        view.background = BitmapDrawable(resources, glassBitmap)
        Log.e("Check", "Using ${System.currentTimeMillis() - startTime}ms")
    }

    private fun getDrawingOffset(isOutBound: Boolean, actualSize: Int, visibleSize: Int): Float {
        return if (isOutBound && actualSize > visibleSize) {
            abs(actualSize - visibleSize).toFloat()
        } else {
            0f
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState: Parcelable? = super.onSaveInstanceState()
        superState?.let {
            val state = SavedState(superState)
            state.blurSampleSize = this.blurSampleSize
            return state
        } ?: run {
            return superState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        when (state) {
            is SavedState -> {
                super.onRestoreInstanceState(state.superState)
                this.blurSampleSize = state.blurSampleSize
            }
            else -> {
                super.onRestoreInstanceState(state)
            }
        }
    }

    internal class SavedState : AbsSavedState {
        var blurSampleSize: Float = 0f

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            blurSampleSize = source.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(blurSampleSize)
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

    private fun View.toBitmap(
        config: Bitmap.Config = Bitmap.Config.ARGB_8888,
        sampleSize: Float = 1f,
        clipRect: Rect? = null
    ): Bitmap {
        if (!ViewCompat.isLaidOut(this)) {
            throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
        }
        return Bitmap.createBitmap(width, height, config).applyCanvas {
            translate(-scrollX.toFloat(), -scrollY.toFloat())
            scale(1 / sampleSize, 1 / sampleSize)
            clipRect?.let { rect ->
                clipRect(rect)
            }
            draw(this)
        }
    }
}