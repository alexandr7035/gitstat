package com.alexandr7035.gitstat.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.ViewRepositoryBinding

class RepositoriesAdapter(private val languagesColors: Map<String, Map<String, String>>): RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    private var items: List<RepositoryEntity> = ArrayList()

    private val colorUnknownLanguage = "#C3C3C3"
    private val createdDateFormat = "yyyy-MM-dd"

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

        holder.binding.repoVisibility.text = when (items[position].isPrivate) {
            true -> "Private"
            else -> "Public"
        }

        val stringColor = when(languagesColors[holder.binding.language.text]) {
            null -> colorUnknownLanguage
            else -> languagesColors[holder.binding.language.text]!!["color"]
        }

        val color: Int = when(holder.binding.language.text) {
            "Unknown" -> Color.parseColor(colorUnknownLanguage)
            else -> Color.parseColor(stringColor)
        }

        (holder.binding.languageColorView.background as GradientDrawable).setColor(color)
    }

    class ViewHolder(val binding: ViewRepositoryBinding): RecyclerView.ViewHolder(binding.root)

}