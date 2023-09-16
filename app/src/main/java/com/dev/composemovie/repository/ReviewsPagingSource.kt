package com.dev.composemovie.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dev.composemovie.model.ResultX
import com.dev.composemovie.network.MovieApi

class ReviewsPagingSource(private val api: MovieApi, val token: String,val movieId: Int) : PagingSource<Int,ResultX>(){
    override fun getRefreshKey(state: PagingState<Int, ResultX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultX> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = api.getMovieReviews(token = token, movieId = movieId ,page = position)

            LoadResult.Page(
                data = responseData.results,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if(responseData.results.isEmpty()) null else position +1,
            )
        }catch (e: Exception){
            return LoadResult.Error(e)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}