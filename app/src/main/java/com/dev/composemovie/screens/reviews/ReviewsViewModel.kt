package com.dev.composemovie.screens.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.composemovie.model.GenreXX
import com.dev.composemovie.model.Result
import com.dev.composemovie.model.ResultX
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val repository: MovieRepository):ViewModel() {
    fun getReviewsById(
        movieId: Int
    ): LiveData<PagingData<ResultX>> = repository.getMovieReview(movieId).cachedIn(viewModelScope)
}