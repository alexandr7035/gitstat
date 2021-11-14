package by.alexandr7035.gitstat.core

import android.content.Context
import android.util.AttributeSet
import android.view.View

// Use it with one side with WRAP_CONTENT
class ContributionCeilView: View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, widthSize)
        }
        else if(heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(heightSize, heightSize)
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}