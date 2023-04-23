package by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.alexandr7035.gitstat.databinding.ViewCardPinnedRepoBinding
import by.alexandr7035.gitstat.databinding.ViewCardRepoMetricBinding

class ReposScrollableBarAdapter(private val items: List<ReposBarItems>) : RecyclerView.Adapter<ReposScrollableBarAdapter.BarViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ReposBarItems.PinnedRepo -> VIEW_TYPE_PINNED_REPO
            is ReposBarItems.MetricCard -> VIEW_TYPE_METRIC_CARD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        return when (viewType) {
            VIEW_TYPE_METRIC_CARD -> {
                val binding = ViewCardRepoMetricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BarViewHolder.MetricCardViewHolder(binding)
            }

            VIEW_TYPE_PINNED_REPO -> {
                val binding = ViewCardPinnedRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BarViewHolder.PinnedRepoViewHolder(binding)
            }

            else -> throw IllegalArgumentException("View type not implemented")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        holder.bind(items[position])
    }

    abstract class BarViewHolder(open val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: ReposBarItems)

        class MetricCardViewHolder(override val binding: ViewCardRepoMetricBinding) : BarViewHolder(binding) {
            override fun bind(item: ReposBarItems) {
                item as ReposBarItems.MetricCard
                binding.repoName.text = item.repoName
                binding.metricName.text = item.metricName
                binding.metricValue.text = "${item.metricValue}"
            }
        }

        class PinnedRepoViewHolder(override val binding: ViewCardPinnedRepoBinding) : BarViewHolder(binding) {
            override fun bind(item: ReposBarItems) {
                item as ReposBarItems.PinnedRepo
                // FIXME
                binding.repoName.text = "${item.repoName}\nPinned"
            }
        }
    }


    companion object {
        private const val VIEW_TYPE_METRIC_CARD = 0
        private const val VIEW_TYPE_PINNED_REPO = 1
    }
}