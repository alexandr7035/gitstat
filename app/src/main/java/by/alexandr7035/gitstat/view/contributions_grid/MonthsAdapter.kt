package by.alexandr7035.gitstat.view.contributions_grid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.data.local.model.ContributionsMonthWithDays
import by.alexandr7035.gitstat.databinding.ViewMonthContributionsGridBinding
import by.alexandr7035.gitstat.extensions.debug
import timber.log.Timber

class MonthsAdapter: RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

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
        holder.binding.monthCardTitle.text = items[position].month.id.toString()
    }

    class ViewHolder(val binding: ViewMonthContributionsGridBinding): RecyclerView.ViewHolder(binding.root)
}