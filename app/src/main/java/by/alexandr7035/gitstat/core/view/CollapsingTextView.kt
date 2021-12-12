package by.alexandr7035.gitstat.core.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class CollapsingTextView(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs), View.OnClickListener {
    private var predefinedMaxLines = 0

    init {
        predefinedMaxLines = maxLines

        // If want to collapse on clicks directly on the view
        // Otherwise use toggle() method
        if (isClickable) {
            super.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        toggle()
    }

    fun toggle() {
        maxLines = if (maxLines == predefinedMaxLines) {
            Int.MAX_VALUE
        } else {
            predefinedMaxLines
        }
    }
}