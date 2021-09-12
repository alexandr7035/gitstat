package com.alexandr7035.gitstat.view.filters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.databinding.ViewLanguageTagBinding

class LanguagesAdapter: RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private var items: List<Language> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<Language>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewLanguageTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguagesAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.languageNameView.text = items[position].name
        (holder.binding.languageTagColor.background as GradientDrawable).setColor(Color.parseColor(items[position].color))
    }


    class ViewHolder(val binding: ViewLanguageTagBinding): RecyclerView.ViewHolder(binding.root)

}