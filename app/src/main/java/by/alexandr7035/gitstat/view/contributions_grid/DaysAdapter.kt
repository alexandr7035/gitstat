package by.alexandr7035.gitstat.view.contributions_grid

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.BuildConfig
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.TimeHelper
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.databinding.ViewContributionsGridCellBinding
import by.alexandr7035.gitstat.extensions.debug
import timber.log.Timber

class DaysAdapter(private val dayClickListener: DayClickListener): RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

    private var items: List<ContributionDayEntity> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ContributionDayEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewContributionsGridCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.binding.root.background as GradientDrawable).setColor(items[position].color)

        // FIXME
        val timeHelper = TimeHelper()

        if (items[position].date == timeHelper.getBeginningOfDayForUnixDate_currentTz(System.currentTimeMillis())) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.binding.root.foreground =
                    ContextCompat.getDrawable(holder.binding.root.context, R.drawable.foreground_contributions_grid_cell_current_day)
            }
        }
    }

    inner class ViewHolder(val binding: ViewContributionsGridCellBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val clickedDay = items[adapterPosition]
            Timber.debug("clicked $clickedDay")
            dayClickListener.onDayItemClick(clickedDay)
        }
    }

}