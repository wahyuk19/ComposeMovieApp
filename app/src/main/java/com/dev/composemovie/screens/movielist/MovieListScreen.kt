package com.dev.composemovie.screens.movielist

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun MovieListScreen(navController: NavController,genreId: Int,viewModel: MovieListViewModel = hiltViewModel()){

    Log.d("TAG", "MovieListScreen: genre id $genreId")
}