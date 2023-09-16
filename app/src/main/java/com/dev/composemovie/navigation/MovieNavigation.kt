package com.dev.composemovie.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.dev.composemovie.screens.MovieSplashScreen
import com.dev.composemovie.screens.details.MovieDetailScreen
import com.dev.composemovie.screens.genres.MovieGenreScreen
import com.dev.composemovie.screens.genres.MovieGenreViewModel
import com.dev.composemovie.screens.movielist.MovieListScreen
import com.dev.composemovie.screens.reviews.ReviewsScreen

@Composable
fun MovieNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MovieScreens.SplashScreen.name ){
        composable(MovieScreens.SplashScreen.name){
            MovieSplashScreen(navController = navController)
        }
        composable(MovieScreens.GenresScreen.name){
            val genreListViewModel = hiltViewModel<MovieGenreViewModel>()
            MovieGenreScreen(navController = navController,viewModel = genreListViewModel)
        }
        val movieList = MovieScreens.MovieListScreen.name
        composable("$movieList/{genreId}", arguments = listOf(navArgument("genreId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("genreId").let {
                MovieListScreen(navController = navController, genreId = it.toString())
            }

        }
        val movieDetail = MovieScreens.MovieDetailScreen.name
        composable("$movieDetail/{movieId}", arguments = listOf(navArgument("movieId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("movieId").let {
                MovieDetailScreen(navController = navController, movieId = it.toString())
            }
        }
        val reviews = MovieScreens.ReviewsScreen.name
        composable("$reviews/{movieId}", arguments = listOf(navArgument("movieId"){
            type = NavType.StringType
        })){backStackEntry ->
            backStackEntry.arguments?.getString("movieId").let {
                ReviewsScreen(navController = navController, movieId = it.toString())
            }
        }
    }
}