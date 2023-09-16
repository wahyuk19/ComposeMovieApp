package com.dev.composemovie.screens.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.composemovie.model.Result
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    fun getMoviesByGenre(genreId: Int): MutableStateFlow<PagingData<Result>>{
        loadMovies(genreId)
        return _moviesState
    }

    private val _moviesState: MutableStateFlow<PagingData<Result>> = MutableStateFlow(value = PagingData.empty())

    private fun loadMovies(genreId: Int) {
        viewModelScope.launch {
            getMovies(genreId)
        }
    }

    private suspend fun getMovies(genreId: Int) {
        repository.getMovies(genreId).distinctUntilChanged().cachedIn(viewModelScope).collect{
            _moviesState.value = it
        }
    }


}