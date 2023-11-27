package com.allacsta.storyapp.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.allacsta.storyapp.data.api.ListStory
import com.allacsta.storyapp.databinding.ItemRowStoryBinding
import com.bumptech.glide.Glide

class StoryAdapter: PagingDataAdapter<ListStory, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {


    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MyViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(story: ListStory){

            itemView.setOnClickListener{
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "photo"),
                        Pair(binding.tvItemName, "name"),
                        Pair(binding.tvItemDescription, "description")
                    )
                onItemClickCallback.onItemClicked(story, optionsCompat)
            }

            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.MyViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
//        holder.bind(listStory[position])
    }

//    override fun getItemCount() = listStory.size

//    fun setList(story: List<ListStory>) {
//        listStory.clear()
//        listStory.addAll(story)
//        notifyDataSetChanged()
//    }

    interface OnItemClickCallback {
        fun onItemClicked(story: ListStory, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}