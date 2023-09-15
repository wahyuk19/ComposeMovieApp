package com.dev.composemovie.navigation

enum class MovieScreens {
    SplashScreen,
    GenresScreen,
    MovieListScreen,
    MovieDetailScreen,
    ReviewScreen;

    companion object {
        fun fromRoute(route: String):MovieScreens
        = when(route.substringBefore("/")){
            SplashScreen.name -> SplashScreen
            GenresScreen.name -> GenresScreen
            MovieListScreen.name -> MovieListScreen
            MovieDetailScreen.name -> MovieDetailScreen
            ReviewScreen.name -> ReviewScreen
            else -> throw IllegalArgumentException("Route $route is recognized")
        }
    }
}