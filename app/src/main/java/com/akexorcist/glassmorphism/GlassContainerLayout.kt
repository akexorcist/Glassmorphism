package com.akexorcist.glassmorphism

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.drawToBitmap

class GlassContainerLayout : FrameLayout {
    private val renderScript: RenderScript by lazy { RenderScript.create(context) }
    private val backgroundViewGroup: View by lazy { getChildAt(0) }
    private val glassBackgroundView: GlassBackgroundView by lazy { getChildAt(1) as GlassBackgroundView }
    private val foregroundViewGroup: View by lazy { getChildAt(2) }

    private val blurPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private lateinit var backgroundBitmap: Bitmap
    private lateinit var pixels: IntArray
    private var blurAmount: Int = Blur.NORMAL.amount
    private var sampleSize = SampleSize.BEST.size

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        setupStyleable(context, attrs)
    }


    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlassContainerLayout)
        this.blurAmount = typedArray.getInteger(R.styleable.GlassContainerLayout_glassContainerLayout_blurAmount, Blur.NORMAL.amount)
        this.sampleSize = typedArray.getInteger(R.styleable.GlassContainerLayout_glassContainerLayout_blurSampleSize, SampleSize.BEST.size)
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        verifyChild()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        glassify()
    }

    @Suppress("unused")
    fun setBlurAmount(blur: Blur) {
        blurAmount = blur.amount
    }

    @Suppress("unused")
    fun getBlurAmount(): Blur {
        return when (blurAmount) {
            Blur.MIN.amount -> Blur.MIN
            Blur.LOW.amount -> Blur.LOW
            Blur.NORMAL.amount -> Blur.NORMAL
            Blur.HIGH.amount -> Blur.HIGH
            Blur.MAX.amount -> Blur.MAX
            else -> Blur.NORMAL
        }
    }

    @Suppress("unused")
    fun setSampleSize(size: SampleSize) {
        sampleSize = size.size
    }

    @Suppress("unused")
    fun getSampleSize(): SampleSize {
        return when (blurAmount) {
            SampleSize.BEST.size -> SampleSize.BEST
            SampleSize.GOOD.size -> SampleSize.GOOD
            SampleSize.POOR.size -> SampleSize.POOR
            else -> SampleSize.BEST
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun updateBackground() {
        backgroundBitmap = updateBackgroundBitmap()
        blur(backgroundBitmap, blurAmount)
        pixels = IntArray(backgroundBitmap.height * backgroundBitmap.width)
    }

    private fun updateBackgroundBitmap(): Bitmap {
        return backgroundViewGroup.drawToBitmap().let {
            Bitmap.createScaledBitmap(
                it,
                it.width / sampleSize,
                it.height / sampleSize,
                false
            )
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun glassify() {
        if (!this::backgroundBitmap.isInitialized) {
            updateBackground()
        }
        val timeStart = System.currentTimeMillis()
        val foregroundBitmap = foregroundViewGroup.drawToBitmap().let {
            Bitmap.createScaledBitmap(
                it,
                it.width / sampleSize,
                it.height / sampleSize,
                false
            )
        }
        foregroundBitmap.getPixels(
            pixels,
            0,
            foregroundBitmap.width,
            0,
            0,
            foregroundBitmap.width,
            foregroundBitmap.height
        )
        pixels.forEachIndexed { index, pixel ->
            when {
                pixel and 0x111111 == 0 && pixel shr 24 > 0 ->
                    pixels[index] = Color.TRANSPARENT
                pixel != 0 ->
                    pixels[index] = Color.WHITE
            }
        }
        foregroundBitmap.setPixels(
            pixels,
            0,
            foregroundBitmap.width,
            0,
            0,
            foregroundBitmap.width,
            foregroundBitmap.height
        )
        Canvas(foregroundBitmap).apply {
            drawBitmap(backgroundBitmap, 0f, 0f, blurPaint)
        }
        glassBackgroundView.setImageBitmap(foregroundBitmap)
        Log.e("Check", "All done : ${System.currentTimeMillis() - timeStart}ms")
    }

    private fun verifyChild() {
        if (childCount != 3) {
            throw RuntimeException("3 view groups inside glass container layout is required")
        }
        if (getChildAt(1) !is GlassBackgroundView) {
            throw RuntimeException("View at index 1 should be GlassBackgroundView")
        }
    }

    @Suppress("SameParameterValue")
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

    enum class Blur(val amount: Int) {
        MIN(4),
        LOW(6),
        NORMAL(8),
        HIGH(10),
        MAX(12)
    }

    enum class SampleSize(val size: Int) {
        BEST(4),
        GOOD(8),
        POOR(16)
    }
}