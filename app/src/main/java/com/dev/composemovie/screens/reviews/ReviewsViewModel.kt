package com.dev.composemovie.screens.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.composemovie.model.ResultX
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val repository: MovieRepository):ViewModel() {

    fun getMovieReviewById(movieId: Int): MutableStateFlow<PagingData<ResultX>> {
        loadReview(movieId)
        return _reviewState
    }

    private val _reviewState: MutableStateFlow<PagingData<ResultX>> = MutableStateFlow(value = PagingData.empty())

    private fun loadReview(movieId: Int) {
        viewModelScope.launch {
            getReviews(movieId)
        }
    }

    private suspend fun getReviews(movieId: Int) {
        repository.getMovieReview(movieId).distinctUntilChanged().cachedIn(viewModelScope).collect{
            _reviewState.value = it
        }
    }
}