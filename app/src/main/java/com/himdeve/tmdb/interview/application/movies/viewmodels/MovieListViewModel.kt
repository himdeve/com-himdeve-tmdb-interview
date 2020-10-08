package com.himdeve.tmdb.interview.application.movies.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.domain.movies.repository.IMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

/**
 * Created by Himdeve on 9/27/2020.
 */
@ExperimentalCoroutinesApi
class MovieListViewModel @ViewModelInject constructor(
    private val movieRepository: IMovieRepository
) : ViewModel() {
    val movies: LiveData<DataState<List<Movie>>>
        get() = _moviesMutable
    private val _moviesMutable: MutableLiveData<DataState<List<Movie>>> = MutableLiveData()

    var inMemorySavedMovies: List<Movie> = listOf()
        private set

    private var _startDate: Calendar? = null
    private var _endDate: Calendar? = null

    init {
        fetchMovies()
    }

    // null values means no restriction
    fun setDate(startDate: Calendar?, endDate: Calendar?) {
        _startDate = startDate
        _endDate = endDate

        fetchMovies()
    }

    fun refresh() {
        fetchMovies()
    }

    private fun fetchMovies() {
        movieRepository.getMovies(
            startDate = _startDate,
            endDate = _endDate
        ).onEach { moviesRes ->
            saveMovies(moviesRes)
            _moviesMutable.value = moviesRes
        }.launchIn(viewModelScope)
    }

    // TODO: figure out a better way for screen rotation (ViewModel) if there was a network error,
    //  but database data are available. Maybe to use another live data observer in fragment
    private fun saveMovies(moviesRes: DataState<List<Movie>>) {
        if (moviesRes is DataState.Success) {
            moviesRes.data.let { movies ->
                inMemorySavedMovies = movies
            }
        }
    }
}