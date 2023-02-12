package com.antn.openmoviedemo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.DifferCallback
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.antn.openmoviedemo.R
import com.antn.openmoviedemo.data.Movie
import com.antn.openmoviedemo.databinding.ItemMovieBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieAdapter: PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffUtilCallback()) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.tvTitle.text = item.title
            binding.tvYear.text = item.year.toString()
            Glide.with(itemView).load(item.poster)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_hide_image_24)
                .into(binding.imgPoster)
        }
    }

    class MovieDiffUtilCallback: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}