package by.alexandr7035.gitstat.view.contributions_grid

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.data.local.model.ContributionDayEntity
import by.alexandr7035.gitstat.databinding.ViewContributionsGridCellBinding

class DaysAdapter: RecyclerView.Adapter<DaysAdapter.ViewHolder>() {

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
//        TODO("Not yet implemented")
        (holder.binding.root.background as GradientDrawable).setColor(items[position].color)
    }

    class ViewHolder(val binding: ViewContributionsGridCellBinding): RecyclerView.ViewHolder(binding.root)
}