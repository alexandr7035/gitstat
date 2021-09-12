package com.alexandr7035.gitstat.view.filters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.core.CheckableLinearLayout
import com.alexandr7035.gitstat.core.Language
import com.alexandr7035.gitstat.databinding.ViewLanguageTagBinding

class LanguagesAdapter(checkedLanguages: HashSet<String>): RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private var items: List<Language> = ArrayList()
    private var checkedItems: HashSet<String> = checkedLanguages

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Language>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getCheckedLanguages(): HashSet<String> {
        return checkedItems
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearCheckedLanguages() {
        checkedItems = HashSet()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewLanguageTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.languageNameView.text = items[position].name
        (holder.binding.languageTagColor.background as GradientDrawable).setColor(Color.parseColor(items[position].color))

        holder.binding.root.isChecked = checkedItems.contains(items[position].name)
    }


    inner class ViewHolder(val binding: ViewLanguageTagBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // Pass language name and state

            if ((v as CheckableLinearLayout).isChecked) {
                checkedItems.add(items[adapterPosition].name)
            }
            else {
                checkedItems.remove(items[adapterPosition].name)
            }

        }
    }

}