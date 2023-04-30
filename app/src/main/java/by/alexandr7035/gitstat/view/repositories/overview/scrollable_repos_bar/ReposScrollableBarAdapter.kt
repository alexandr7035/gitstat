package by.alexandr7035.gitstat.view.repositories.overview.scrollable_repos_bar

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.alexandr7035.gitstat.databinding.ViewCardPinnedRepoBinding
import by.alexandr7035.gitstat.databinding.ViewCardRepoMetricBinding

class ReposScrollableBarAdapter(private val repoClickCallback: (repositoryId: Int) -> Unit) : RecyclerView.Adapter<ReposScrollableBarAdapter.BarViewHolder>() {

    private var items: List<ReposBarItems> = emptyList()

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
                BarViewHolder.PinnedRepoViewHolder(binding, repoClickCallback)
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

        class PinnedRepoViewHolder(
            override val binding: ViewCardPinnedRepoBinding,
            private val repoClickCallback: (repositoryId: Int) -> Unit
        ) : BarViewHolder(binding) {
            override fun bind(item: ReposBarItems) {
                item as ReposBarItems.PinnedRepo
                binding.repoName.text = item.repoName

                val bgDrawable = binding.root.background.constantState?.newDrawable()?.mutate() as GradientDrawable
                val colorRes = ContextCompat.getColor(binding.root.context, item.bgColorRes)

//                val colorsList = intArrayOf(colorRes, ColorUtils.setAlphaComponent(colorRes, 1))
                // Choose color 20% more dark
                val colorsList = intArrayOf(colorRes, ColorUtils.blendARGB(colorRes, Color.BLACK, 0.3f))

                bgDrawable.colors = colorsList
                bgDrawable.orientation = GradientDrawable.Orientation.BL_TR
                bgDrawable.setGradientCenter(0.8f, 0.8f)

                binding.root.background = bgDrawable

                // Lang
                binding.repoLanguage.text = item.repoLang
                (binding.langContainer.background as GradientDrawable).setColor(Color.WHITE)
                (binding.languageColorView.background as GradientDrawable).setColor(Color.parseColor(item.repoLangColor))

                // Stars
                binding.stars.text = item.stars.toString()

                binding.root.setOnClickListener {
                    repoClickCallback.invoke(item.repoId)
                }
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ReposBarItems>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_METRIC_CARD = 0
        private const val VIEW_TYPE_PINNED_REPO = 1
    }
}