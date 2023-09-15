package com.dev.composemovie.network

import com.dev.composemovie.model.Genre
import com.dev.composemovie.model.MovieDetails
import com.dev.composemovie.model.MovieDiscover
import com.dev.composemovie.model.Reviews
import com.dev.composemovie.model.Videos
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface MovieApi {

    @GET("genre/movie/list")
    suspend fun getGenreList(
        @Header("authorization") token: String,
        @Query("language") language: String? = "en"
    ): Genre

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Header("authorization") token: String,
        @Query("include_adult") includeAdult: Boolean? = false,
        @Query("include_video") includeVideo: Boolean? = false,
        @Query("language") language: String? = "en-US",
        @Query("with_genres") genre: Int,
        @Query("page") page: Int,
    ): MovieDiscover

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("authorization") token: String,
        @Path("movie_id") path: Int,
        @Query("language") language: String? = "en-US",
    ): MovieDetails

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Header("authorization") token: String,
        @Path("movie_id") movieId: Int,
        @Query("language") language: String? = "en-US",
        @Query("page") page: Int
    ): Reviews


    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Header("authorization") token: String,
        @Path("movie_id") path: Int,
        @Query("language") language: String? = "en-US",
    ): Videos
}