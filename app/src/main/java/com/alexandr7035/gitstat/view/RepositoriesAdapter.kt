package com.alexandr7035.gitstat.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexandr7035.gitstat.data.local.model.RepositoryEntity
import com.alexandr7035.gitstat.databinding.ViewRepositoryBinding

class RepositoriesAdapter: RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    private var items: List<RepositoryEntity> = ArrayList()

    fun setItems(items: List<RepositoryEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.view_repository, parent, false)
        val binding = ViewRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.itemView.name = repos[position].name
        holder.binding.repoName.text = items[position].name
        holder.binding.createdDate.text = "2017-02-19"
        holder.binding.language.text = items[position].language
        holder.binding.stars.text = "100"

        holder.binding.repoVisibility.text = when (items[position].isPrivate) {
            true -> "Private"
            else -> "Public"
        }

    }

    class ViewHolder(val binding: ViewRepositoryBinding): RecyclerView.ViewHolder(binding.root)

}