package com.dev.composemovie.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dev.composemovie.BuildConfig
import com.dev.composemovie.data.Resource
import com.dev.composemovie.model.GenreX
import com.dev.composemovie.model.MovieDetails
import com.dev.composemovie.model.MovieDiscover
import com.dev.composemovie.model.Result
import com.dev.composemovie.model.ResultX
import com.dev.composemovie.model.Videos
import com.dev.composemovie.network.MovieApi
import javax.inject.Inject

class MovieRepository @Inject constructor(private val api: MovieApi) {
    suspend fun getGenres(): Resource<List<GenreX>>{
        return try {
            Resource.Loading(data = true)

            val itemList = api.getGenreList(BuildConfig.API_TOKEN).genres
            if(itemList.isNotEmpty()) Resource.Loading(data = false)

            Resource.Success(data = itemList)
        }catch (e: Exception){
            Resource.Error(message = e.message.toString())
        }
    }

    fun getMovies(genre: Int) : LiveData<PagingData<Result>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                MoviePagingSource(api,BuildConfig.API_TOKEN, genre = genre)
            }
        ).liveData
    }

    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>{
        val response = try{
            Resource.Loading(data = true)
            api.getMovieDetails(BuildConfig.API_TOKEN,movieId)
        }catch (e: Exception){
            return Resource.Error(message = "An error occured ${e.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    suspend fun getMovieVideo(movieId: Int): Resource<Videos>{
        val response = try{
            Resource.Loading(data = true)
            api.getMovieVideos(BuildConfig.API_TOKEN,movieId)
        }catch (e: Exception){
            return Resource.Error(message = "An error occured ${e.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }

    fun getMovieReview(movieId: Int) : LiveData<PagingData<ResultX>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ReviewsPagingSource(api,BuildConfig.API_TOKEN,movieId)
            }
        ).liveData
    }

}