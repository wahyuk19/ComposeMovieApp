package com.dev.composemovie.screens.details

import androidx.lifecycle.ViewModel
import com.dev.composemovie.data.Resource
import com.dev.composemovie.model.MovieDetails
import com.dev.composemovie.model.Videos
import com.dev.composemovie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val repository: MovieRepository): ViewModel() {
    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>{
        return repository.getMovieDetails(movieId)
    }

    suspend fun getVideo(movieId: Int): Resource<Videos>{
        return repository.getMovieVideo(movieId)
    }
}