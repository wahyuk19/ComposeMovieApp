package com.dev.composemovie.screens.movielist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.composemovie.data.Resource
import com.dev.composemovie.model.Result
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
   fun getMoviesByGenre(
       genreId: Int
   ): LiveData<PagingData<Result>> = repository.getMovies(genreId).cachedIn(viewModelScope)
}