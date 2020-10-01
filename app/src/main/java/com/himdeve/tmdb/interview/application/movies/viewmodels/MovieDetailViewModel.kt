package com.himdeve.tmdb.interview.application.movies.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.himdeve.tmdb.interview.domain.core.DataState
import com.himdeve.tmdb.interview.domain.movies.model.Movie
import com.himdeve.tmdb.interview.domain.movies.repository.IMovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Created by Himdeve on 9/29/2020.
 */

private const val MOVIE_ID_ARG_KEY = "movieId"

@ExperimentalCoroutinesApi
class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepository: IMovieRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val movieDetail: LiveData<DataState<Movie>>
        get() = _movieDetailMutable

    private val _movieDetailMutable: MutableLiveData<DataState<Movie>> = MutableLiveData()

    init {
        // Don't forget to pass movieId argument to MovieDetailFragment!
        val movieId = savedStateHandle.get<Int>(MOVIE_ID_ARG_KEY)!!

        viewModelScope.launch {
            movieRepository.getMovie(movieId).onEach { movieDetailRes ->
                _movieDetailMutable.value = movieDetailRes
            }.launchIn(this)
        }
    }
}