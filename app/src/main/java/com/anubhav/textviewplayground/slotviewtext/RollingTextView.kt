package com.anubhav.textviewplayground.slotviewtext

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.anubhav.textviewplayground.R
import kotlin.math.abs

class RollingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textSize: Float = 60f
    private var textColor: Int = Color.BLACK
    private var fontFamily: Typeface = Typeface.MONOSPACE

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var currentNumber: Int = 0
    private var targetNumber: Int = 0

    private var progress: Float = 0f
    private var isIncreasing: Boolean = true

    private var animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 120
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.RollingTextView) {
            textSize = getDimension(R.styleable.RollingTextView_android_textSize, textSize)
            textColor = getColor(R.styleable.RollingTextView_android_textColor, textColor)
            fontFamily =
                try {
                    Typeface.createFromAsset(
                        context.assets,
                        getString(R.styleable.RollingTextView_android_fontFamily)
                    )
                } catch (e: java.lang.Exception) {
                    try {
                        Typeface.create(
                            getString(R.styleable.RollingTextView_android_fontFamily),
                            Typeface.NORMAL
                        )
                    } catch (e: java.lang.Exception) {
                        Typeface.MONOSPACE
                    }
                }
        }

        paint.textSize = textSize
        paint.color = textColor
        paint.typeface = fontFamily
        paint.textAlign = Paint.Align.CENTER
    }

    fun setNumber(number: Int) {
        targetNumber = number
        isIncreasing = targetNumber > currentNumber
        animator.addUpdateListener { valueAnimator ->
            progress = abs(valueAnimator.animatedValue as Float)
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val midX = width / 2f
        val midY = height / 2f

        val y1 = if (isIncreasing) midY - progress * height else midY + progress * height
        val y2 = if (isIncreasing) height.toFloat() + midY - progress * height else -height.toFloat() + midY + progress * height

        val alpha1 = ((1 - progress) * 255).toInt().coerceIn(0, 255)
        val alpha2 = (progress * 255).toInt().coerceIn(0, 255)

        paint.alpha = alpha1
        canvas.drawText(currentNumber.toString(), midX, y1, paint)

        paint.alpha = alpha2
        canvas.drawText(targetNumber.toString(), midX, y2, paint)

        if (progress >= 1f) {
            currentNumber = targetNumber
            animator.removeAllUpdateListeners()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredSize = (textSize * 2).toInt()

        val finalWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> width
            MeasureSpec.AT_MOST -> desiredSize.coerceAtMost(width)
            else -> desiredSize
        }

        val finalHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> height
            MeasureSpec.AT_MOST -> desiredSize.coerceAtMost(height)
            else -> desiredSize
        }

        setMeasuredDimension(finalWidth, finalHeight)
    }
}
