package com.alexandr7035.gitstat.view.contributions.plots.contributions_ratio

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.databinding.ViewRatioPlotLegendItemBinding

class RatioLegendAdapter: RecyclerView.Adapter<RatioLegendAdapter.ViewHolder>() {

    private var items: List<RatioLegendItem> = emptyList()

    class ViewHolder(val binding: ViewRatioPlotLegendItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRatioPlotLegendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // FIXME test
        holder.binding.countView.text = items[position].count.toString()
        holder.binding.label.text = items[position].label
        holder.binding.percentageView.text = "${items[position].percentage}%"
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<RatioLegendItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}