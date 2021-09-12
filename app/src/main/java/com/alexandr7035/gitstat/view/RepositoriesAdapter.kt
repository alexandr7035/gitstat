package com.alexandr7035.gitstat.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.R
import com.alexandr7035.gitstat.core.ProgLangManager
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.ViewRepositoryBinding

class RepositoriesAdapter(private val langManager: ProgLangManager): RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    private var items: List<RepositoryEntity> = ArrayList()
    private val createdDateFormat = "yyyy-MM-dd"

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<RepositoryEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.repoName.text = items[position].name
        holder.binding.createdDate.text = DateFormat.format(createdDateFormat, items[position].created_at)
        holder.binding.language.text = items[position].language
        holder.binding.stars.text = items[position].stars.toString()

        when (items[position].isPrivate) {
            true -> {
                holder.binding.repoVisibility.text = holder.itemView.context.getString(R.string.private_start_capital)
                holder.binding.repoVisibility.setTextColor(ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                ))

                holder.binding.repoVisibility.background = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.background_repo_visibilily_private)
            }
            else -> {
                holder.binding.repoVisibility.text = holder.itemView.context.getString(R.string.public_start_capital)
                holder.binding.repoVisibility.setTextColor(ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.black
                ))
                holder.binding.repoVisibility.background = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.background_repo_visibilily)
            }
        }

        val color = langManager.getLanguageColor(language = holder.binding.language.text as String)
        (holder.binding.languageColorView.background as GradientDrawable).setColor(Color.parseColor(color))
    }

    class ViewHolder(val binding: ViewRepositoryBinding): RecyclerView.ViewHolder(binding.root)

}