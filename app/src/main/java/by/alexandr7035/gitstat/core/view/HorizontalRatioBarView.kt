package by.alexandr7035.gitstat.core.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import by.alexandr7035.gitstat.extensions.debug
import timber.log.Timber

class HorizontalRatioBarView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private val paint = Paint()

    private var colors: List<Int>? = null
    private var values: List<Float>? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        paint.color = Color.RED
        paint.strokeWidth = 10f
    }

    // Called on first time too
    // Calculate sizes here
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Timber.debug("customView onDraw()")

        if ((! colors.isNullOrEmpty()) && (! values.isNullOrEmpty())) {
            Timber.debug("customView onDraw() params not null")

            values = listOf(50f, 50f)
            val total = values!!.sum()

            var inset = 0f

            val spacing = 20f
            val realWidth = width - spacing * (values!!.size - 1)

            for (bar in values!!) {
                val valuePerc = bar / total

                Timber.debug("draw width $width")

                Timber.debug("draw valuePerc $valuePerc")

                val rectLeft = inset
                val rectTop = 0f
                val rectRight = inset + valuePerc*realWidth
                val rectBottom = height.toFloat()

                Timber.debug("draw rect $rectLeft $rectTop $rectRight $rectBottom")

                val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)

                canvas.drawRoundRect(
                    rect,
                    10f,
                    10f,
                    paint
                )

                inset += rect.width()
                inset += spacing
            }
        }

    }


    fun setColors(colors: List<Int>) {
        this.colors = colors
    }

    fun setValues(values: List<Float>) {
        this.values = values
    }
}