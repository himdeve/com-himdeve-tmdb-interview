package com.himdeve.tmdb.interview.presentation.movies.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.himdeve.tmdb.interview.R
import com.himdeve.tmdb.interview.application.movies.viewmodels.MovieDetailViewModel
import com.himdeve.tmdb.interview.databinding.FragmentMovieDetailBinding
import com.himdeve.tmdb.interview.domain.core.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


/**
 * Created by Himdeve on 9/29/2020.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        // Invoke functionality of options menu for this fragment
        setHasOptionsMenu(true)

        subscribeUi(binding)

        return binding.root
    }

    // hide options menu items
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem = menu.findItem(R.id.action_settings)
        item.isVisible = false
    }

    private fun subscribeUi(binding: FragmentMovieDetailBinding) {
        viewModel.movieDetail.observe(viewLifecycleOwner) {
            when (it.status) {
                DataState.Status.LOADING ->
                    binding.includedContentMovieDetail.progressBar.visibility = View.VISIBLE
                DataState.Status.SUCCESS -> {
                    binding.includedContentMovieDetail.progressBar.visibility = View.GONE
                    it.data?.let { movie ->
                        binding.movie = movie
                    }
                }
                DataState.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}