package com.dev.composemovie.model

data class MovieDiscover(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)