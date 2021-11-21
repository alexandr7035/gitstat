package by.alexandr7035.gitstat.view.repositories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.alexandr7035.gitstat.databinding.ViewRepositoryTopicBinding

class RepoTopicsAdapter: RecyclerView.Adapter<RepoTopicsAdapter.ViewHolder>() {

    private var items: List<String> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewRepositoryTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.text = items[position]
    }

    class ViewHolder(val binding: ViewRepositoryTopicBinding): RecyclerView.ViewHolder(binding.root)

}