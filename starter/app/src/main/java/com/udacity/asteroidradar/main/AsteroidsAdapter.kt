package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ListViewItemBinding

class AsteroidsAdapter(val onClickListener: OnClickListener) : ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    var asteroids: List<Asteroid> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val withDataBinding: ListViewItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
        AsteroidViewHolder.LAYOUT,
        parent,
        false)
        return AsteroidViewHolder(withDataBinding)
    }

    override fun getItemCount() = asteroids.size

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val currentAsteroid = asteroids[position]
        holder.bind(currentAsteroid)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentAsteroid)
        }
        holder.viewDataBinding.also {
            it.asteroid = currentAsteroid
        }
    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}

class AsteroidViewHolder(val viewDataBinding: ListViewItemBinding):
    RecyclerView.ViewHolder(viewDataBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.list_view_item
    }

    fun bind(asteroid: Asteroid) {
        viewDataBinding.asteroid = asteroid
        viewDataBinding.executePendingBindings()
    }
}