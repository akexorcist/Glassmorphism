package com.akexorcist.glassmorphism

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.customview.view.AbsSavedState

@Suppress("MemberVisibilityCanBePrivate")
class GlassView : FrameLayout {
    var cornerRadius: Float = 25f
    var padding: Float = 24f
    var surfaceOpacity: Float = 0.15f
    var borderOpacity: Float = 0.28f
    var shadowOpacity: Float = 0.19f
    var borderWidth: Float = 3f
    var blurAmount: Int = 25

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
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    private fun setupStyleable(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GlassView)
        this.cornerRadius = typedArray.getDimension(R.styleable.GlassView_glassView_cornerRadius, 25f)
        this.padding = typedArray.getDimension(R.styleable.GlassView_glassView_elevation, 24f)
        this.surfaceOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_surfaceOpacity, 0.15f)
        this.borderOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_borderWidth, 0.28f)
        this.shadowOpacity = typedArray.getFloat(R.styleable.GlassView_glassView_shadowOpacity, 0.19f)
        this.borderWidth = typedArray.getDimension(R.styleable.GlassView_glassView_borderWidth, 3f)
        this.blurAmount = typedArray.getInteger(R.styleable.GlassView_glassView_blurAmount, 25)
        typedArray.recycle()
        elevation = this.padding
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