package by.alexandr7035.gitstat.view.contributions_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.data.local.model.ContributionsMonthWithDays
import by.alexandr7035.gitstat.databinding.ViewMonthContributionsGridBinding
import by.alexandr7035.gitstat.core.extensions.debug
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MonthsAdapter(
    private val onDayClick: (ContributionDayEntity) -> Unit
) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

    // FIXME
    private var items: List<ContributionsMonthWithDays> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ContributionsMonthWithDays>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewMonthContributionsGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.debug("month onBindViewHolder")
        val monthId = items[position].month.id.toString()

        val format = SimpleDateFormat("yyyyMM", Locale.US)
        format.timeZone = TimeZone.getTimeZone("GMT")
        // Month id corresponds to date in this format
        val unixDate = format.parse(monthId)!!.time

        val strFormat = SimpleDateFormat("MMMM yyyy", Locale.US)
        strFormat.timeZone = TimeZone.getTimeZone("GMT")
        val dateStr = strFormat.format(unixDate)

        // Moth (e.g. "November 2020")
        holder.binding.monthCardTitle.text = dateStr
        // Contributions count for this month
        holder.binding.monthCardTotalContributions.text = items[position]
            .contributionDays
            .sumOf { it.count }
            .toString()

        val adapter = DaysAdapter(onDayClick = onDayClick)

        holder.binding.cellsRecycler.adapter = adapter
        holder.binding.cellsRecycler.layoutManager = GridLayoutManager(holder.binding.root.context, 7)

        // Set days of the month to cells
        adapter.setItems(items[position].contributionDays)
    }

    class ViewHolder(val binding: ViewMonthContributionsGridBinding) : RecyclerView.ViewHolder(binding.root)
}