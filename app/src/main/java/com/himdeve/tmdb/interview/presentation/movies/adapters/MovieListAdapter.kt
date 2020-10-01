package com.himdeve.tmdb.interview.presentation.movies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.himdeve.tmdb.interview.databinding.ListItemMovieBinding
import com.himdeve.tmdb.interview.domain.movies.model.Movie

/**
 * Created by Himdeve on 9/28/2020.
 */
class MovieListAdapter(
    private val movieItemClickListener: (movieId: Int) -> Unit
) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(
        MovieListDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding, movieItemClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)
        (holder as MovieViewHolder).bind(movie)
    }

    class MovieViewHolder(
        private val binding: ListItemMovieBinding,
        private val movieItemClickListener: (movieId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.movie?.let { movie ->
                    movieItemClickListener(movie.id)
                }
            }
        }

        fun bind(item: Movie) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }
    }
}

private class MovieListDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}