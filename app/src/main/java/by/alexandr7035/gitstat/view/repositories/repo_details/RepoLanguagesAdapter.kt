package by.alexandr7035.gitstat.view.repositories.repo_details

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.data.local.model.RepoLanguage
import by.alexandr7035.gitstat.databinding.ViewRepositoryLanguageBinding

class RepoLanguagesAdapter: RecyclerView.Adapter<RepoLanguagesAdapter.ViewHolder>() {

    private var items: List<RepoLanguage> = emptyList()
    private var totalLanguagesSize = 0

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<RepoLanguage>) {
        this.items = items
        totalLanguagesSize = items.sumOf { it.size }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRepositoryLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.languageNameView.text = items[position].name
        (holder.binding.languageColorView.background as GradientDrawable?)?.setColor(Color.parseColor(items[position].color))

        val percentage = String.format("%.2f", items[position].size.toFloat() / totalLanguagesSize.toFloat() * 100)
        holder.binding.languagePercentage.text = "${percentage}%"
    }

    class ViewHolder(val binding: ViewRepositoryLanguageBinding): RecyclerView.ViewHolder(binding.root)

}