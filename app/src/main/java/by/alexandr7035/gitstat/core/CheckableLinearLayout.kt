package by.alexandr7035.gitstat.core

import android.R.attr.state_checked as attr_state_checked
import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.LinearLayout

class CheckableLinearLayout: LinearLayout, Checkable {

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    companion object {
        private val checkedStateSet = intArrayOf(attr_state_checked)
    }

    private var isChecked: Boolean = false

    override fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            isChecked = checked
            refreshDrawableState()
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(! isChecked)
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }


    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            mergeDrawableStates(drawableState, checkedStateSet)
        }
        return drawableState
    }

}