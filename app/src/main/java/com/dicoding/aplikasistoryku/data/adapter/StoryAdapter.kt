package com.dicoding.aplikasistoryku.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasistoryku.data.response.ListStoryItem
import com.dicoding.aplikasistoryku.databinding.ItemStoryBinding

class StoryAdapter(private var storyList: List<ListStoryItem>, private val clickListener: StoryClickListener) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    interface StoryClickListener {
        fun onStoryClicked(storyId: String)
    }

    fun setStories(stories: List<ListStoryItem>) {
        storyList = stories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
        holder.itemView.setOnClickListener {
            clickListener.onStoryClicked(story.id ?: "")
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                // Set data to views
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(binding.imgPhoto)

                tvName.text = story.name
                tvDescription.text = story.description
            }
        }
    }
}