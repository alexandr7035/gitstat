package by.alexandr7035.gitstat.view.profile.highlights_bar

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import by.alexandr7035.gitstat.core.extensions.debug
import by.alexandr7035.gitstat.databinding.ViewHighlightBinding
import timber.log.Timber

class HighlightsAdapter : RecyclerView.Adapter<HighlightsAdapter.HighlightViewHolder>() {
    private var items: List<HighlightItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<HighlightItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    class HighlightViewHolder(private val binding: ViewHighlightBinding) : ViewHolder(binding.root) {
        fun bind(item: HighlightItem) {
            binding.metric.text = item.metricValue
            binding.metricText.text = item.metricText

            val accentColor = ContextCompat.getColor(binding.root.context, item.accentColor)
            binding.metric.setTextColor(accentColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightViewHolder {
        val binding = ViewHighlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HighlightViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HighlightViewHolder, position: Int) {
        holder.bind(items[position])
    }
}