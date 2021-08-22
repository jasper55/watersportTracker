package wagner.jasper.watersporttracker.utils

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.TextView


class NoPaddingTextView : androidx.appcompat.widget.AppCompatTextView {
    private var mAdditionalPadding = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        setIncludeFontPadding(false)
    }

    override fun onDraw(canvas: Canvas) {
        val yOff = (-mAdditionalPadding / 6).toFloat()
        canvas.translate(0f, yOff)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        additionalPadding
        val mode: Int = MeasureSpec.getMode(heightMeasureSpec)
        if (mode != MeasureSpec.EXACTLY) {
            val measureHeight = measureHeight(getText().toString(), widthMeasureSpec)
            var height = measureHeight - mAdditionalPadding
            height += getPaddingTop() + getPaddingBottom()
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun measureHeight(text: String, widthMeasureSpec: Int): Int {
        val textSize: Float = getTextSize()
        val textView = TextView(getContext())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        textView.setText(text)
        textView.measure(widthMeasureSpec, 0)
        return textView.getMeasuredHeight()
    }

    private val additionalPadding: Int
        private get() {
            val textSize: Float = getTextSize()
            val textView = TextView(getContext())
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            textView.setLines(1)
            textView.measure(0, 0)
            val measuredHeight: Int = textView.getMeasuredHeight()
            if (measuredHeight - textSize > 0) {
                mAdditionalPadding = (measuredHeight - textSize).toInt()
                Log.v(
                    "NoPaddingTextView",
                    "onMeasure: height=$measuredHeight textSize=$textSize mAdditionalPadding=$mAdditionalPadding"
                )
            }
            return mAdditionalPadding
        }
}