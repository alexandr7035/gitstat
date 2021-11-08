package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.databinding.ViewContributionTypesPlotLegendItemBinding

class TypesLegendAdapter: RecyclerView.Adapter<TypesLegendAdapter.ViewHolder>() {

    private var items: List<TypesLegendItem> = emptyList()

    class ViewHolder(val binding: ViewContributionTypesPlotLegendItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewContributionTypesPlotLegendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // FIXME test
        holder.binding.countView.text = items[position].count.toString()
        holder.binding.label.text = items[position].label
        holder.binding.percentageView.text = "${items[position].percentage}%"

        (holder.binding.coloredMark.background as GradientDrawable).setColor(items[position].color)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<TypesLegendItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}