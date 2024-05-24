package com.dicoding.aplikasistoryku.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasistoryku.data.response.ListStoryItem
import com.dicoding.aplikasistoryku.databinding.ItemStoryBinding
import com.dicoding.aplikasistoryku.view.detail.DetailActivity

class StoryAdapter(private var storyList: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

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
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("STORY_ID", story.id ?: "")

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(holder.binding.ivItemPhoto, "photo"),
                    Pair(holder.binding.tvItemName, "name"),
                    Pair(holder.binding.tvDescription, "description")
                )

            context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    inner class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                Glide.with(itemView.context).load(story.photoUrl).into(ivItemPhoto)

                tvItemName.text = story.name
                tvDescription.text = story.description
            }
        }
    }
}