package com.dev.composemovie.navigation

enum class MovieScreens {
    SplashScreen,
    GenresScreen,
    MovieListScreen,
    MovieDetailScreen,
    ReviewsScreen;

    companion object {
        fun fromRoute(route: String):MovieScreens
        = when(route.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            GenresScreen.name -> GenresScreen
            MovieListScreen.name -> MovieListScreen
            MovieDetailScreen.name -> MovieDetailScreen
            ReviewsScreen.name -> ReviewsScreen
            else -> throw IllegalArgumentException("Route $route is recognized")
        }
    }
}