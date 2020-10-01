package com.himdeve.tmdb.interview.presentation.movies.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.himdeve.tmdb.interview.R
import com.himdeve.tmdb.interview.application.movies.viewmodels.MovieListViewModel
import com.himdeve.tmdb.interview.databinding.FragmentMovieListBinding
import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.presentation.movies.adapters.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

/**
 * Created by Himdeve on 9/28/2020.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private val viewModel: MovieListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMovieListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        setSwipeRefresh(binding)

        val adapter = MovieListAdapter { movieId -> onClickedMovie(movieId) }
        // remember a scroll position automatically
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.movieList.adapter = adapter

        subscribeUi(adapter, binding)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("test")
    }

    // null values means no restriction
    fun setDate(startDate: Calendar?, endDate: Calendar?) {
        viewModel.setDate(startDate, endDate)
    }

    private fun onClickedMovie(movieId: Int) {
        val action =
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movieId)
        findNavController().navigate(action)
    }

    private fun subscribeUi(
        adapter: MovieListAdapter,
        binding: FragmentMovieListBinding
    ) {
        viewModel.movies.observe(viewLifecycleOwner) {
            when (it.status) {
                DataState.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.movieNoItems.visibility = View.GONE
                }
                DataState.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    it.data?.let { movies ->
                        adapter.submitList(movies)

                        if (movies.isEmpty()) {
                            binding.movieNoItems.visibility = View.VISIBLE
                        } else {
                            binding.movieNoItems.visibility = View.GONE
                        }
                    }
                }
                DataState.Status.ERROR -> {
                    if (adapter.itemCount == 0) {
                        // TODO: figure out a better way for screen rotation (Fragment)
                        if (viewModel.memorySavedMovies.isNotEmpty()) {
                            // show saved movies
                            adapter.submitList(viewModel.memorySavedMovies)
                        } else {
                            binding.movieNoItems.visibility = View.VISIBLE
                        }
                    }

                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setSwipeRefresh(binding: FragmentMovieListBinding) {
        binding.swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorAccent),
        )

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()

            // we can turn off refresh icon here because we have our own progressBar indicator
            binding.swipeRefresh.isRefreshing = false
        }
    }
}