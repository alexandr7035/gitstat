package by.alexandr7035.gitstat.view.contributions.plots.contributions_types

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.databinding.ViewContributionTypeItemBinding

class TypesAdapter(private val showCount: Boolean = false): RecyclerView.Adapter<TypesAdapter.ViewHolder>() {

    private var items: List<TypesItem> = emptyList()

    class ViewHolder(val binding: ViewContributionTypeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewContributionTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (showCount) {
            holder.binding.countView.text = items[position].count.toString()
        }
        else {
            holder.binding.countView.visibility = View.GONE
        }
        holder.binding.label.text = items[position].label
        holder.binding.percentageView.text = items[position].percentage

        (holder.binding.coloredMark.background as GradientDrawable).setColor(items[position].color)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<TypesItem>) {
        this.items = items
        notifyDataSetChanged()
    }
}