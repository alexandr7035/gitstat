package com.alexandr7035.gitstat.view.filters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.databinding.ViewLanguageTagBinding

class LanguagesAdapter: RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private var items: List<LanguageTag> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<LanguageTag>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewLanguageTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguagesAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.languageNameView.text = items[position].name
        (holder.binding.languageTagColor.background as GradientDrawable).setColor(Color.parseColor("#D35400"))
    }


    class ViewHolder(val binding: ViewLanguageTagBinding): RecyclerView.ViewHolder(binding.root)

}