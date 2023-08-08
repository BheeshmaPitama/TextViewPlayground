package com.anubhav.textviewplayground.custombadgetext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.anubhav.textviewplayground.R

class CustomBadgeView : AppCompatTextView {

    private var badgeColor: Int = ContextCompat.getColor(context, android.R.color.holo_blue_light)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var badgeCircleDiameter: Int = 0
    private var badgeRectangleWidth: Int = 0
    private var badgeRectangleHeight: Int = 0

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomBadgeView)
            badgeColor = typedArray.getColor(R.styleable.CustomBadgeView_badgeColor, badgeColor)

            badgeCircleDiameter = typedArray.getDimensionPixelSize(
                R.styleable.CustomBadgeView_badgeCircleDiameter,
                badgeCircleDiameter
            )

            badgeRectangleWidth = typedArray.getDimensionPixelSize(
                R.styleable.CustomBadgeView_badgeRectangleWidth,
                badgeRectangleWidth
            )

            badgeRectangleHeight = typedArray.getDimensionPixelSize(
                R.styleable.CustomBadgeView_badgeRectangleHeight,
                badgeRectangleHeight
            )

            typedArray.recycle()
        }
        gravity = CENTER
        textAlignment = TEXT_ALIGNMENT_CENTER
        paint.color = badgeColor
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text != null && text.length > 3) {
            this.text = text.subSequence(0, 3)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        if (text?.length == 3) {
            setMeasuredDimension(badgeRectangleWidth, badgeRectangleHeight)
        } else {
            setMeasuredDimension(badgeCircleDiameter, badgeCircleDiameter)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val textLength = text?.length ?: 0
        val cx = width / 2f
        val cy = height / 2f
        val cornerRadius = height / 2f

        if (textLength == 3) {
            canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, paint)
        } else if (textLength in 1..2) {
            val radius = width.coerceAtMost(height) / 2f
            canvas?.drawCircle(cx, cy, radius, paint)
        }
        super.onDraw(canvas)
    }
}
