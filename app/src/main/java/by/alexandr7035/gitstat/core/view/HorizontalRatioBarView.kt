package by.alexandr7035.gitstat.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.extensions.debug
import timber.log.Timber


class HorizontalRatioBarView(context: Context, private val attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()

    private var colors: List<Int>? = null
    private var values: List<Float>? = null

    private var spacingBetweenEntries = 0f
    private var entryCornerRadius = 0f

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.HorizontalRatioBarView, 0, 0
        )

        spacingBetweenEntries = typedArray.getDimension(R.styleable.HorizontalRatioBarView_hrb_entries_between_spacing, 0f)
        entryCornerRadius = typedArray.getDimensionPixelSize(R.styleable.HorizontalRatioBarView_hrb_entry_corner_radius, 0).toFloat()
        Timber.debug("customView onAttachToWindow() spacing $spacingBetweenEntries corners $entryCornerRadius")

        typedArray.recycle()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Timber.debug("customView onDraw()")

        if ((! colors.isNullOrEmpty()) && (! values.isNullOrEmpty())) {

            // Total sum of values
            // Used to calculate percentage for a particular value
            val total = values!!.sum()

            // View width without spacings
            val realWidth = width - spacingBetweenEntries * (values!!.size - 1)

            // Inset is changed for next value
            var inset = 0f

            values!!.forEachIndexed { index, bar ->

                // Calculate value percentage
                val valuePercentage = bar / total

                val rectLeft = inset
                val rectTop = 0f
                // Value pixel size considering percentage and width of view
                val rectRight = inset + valuePercentage*realWidth
                val rectBottom = height.toFloat()

                // Set color
                paint.color = colors!![index]


                // Draw entry
                val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)
                canvas.drawRoundRect(
                    rect,
                    entryCornerRadius,
                    entryCornerRadius,
                    paint
                )

                inset += rect.width()
                inset += spacingBetweenEntries
            }
        }

    }

    fun setValues(values: List<Float>, colors: List<Int>) {

        if (values.size != colors.size) {
            throw IllegalArgumentException("Lengths of values and colors lists MUST be the same!")
        }

        this.values = values
        this.colors = colors
    }
}