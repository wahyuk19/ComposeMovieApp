package com.dev.composemovie.screens.genres

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.composemovie.components.MovieAppBar
import com.dev.composemovie.model.GenreX
import com.dev.composemovie.navigation.MovieScreens
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MovieGenreScreen(navController: NavController,viewModel: MovieGenreViewModel = hiltViewModel()){
    Scaffold(topBar = {
        MovieAppBar(title = "Pick a Genre", navController = navController, showReviews = false, items = null)
    }) {
        Surface {
            Column {
                GenreList(navController = navController,viewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenreList(navController: NavController,viewModel: MovieGenreViewModel) {
    val listOfGenre = viewModel.list
    if(viewModel.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }else if(viewModel.isError){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Error retrieve data\n\n${viewModel.errMsg}", textAlign = TextAlign.Center)
            Button(onClick = {navController.navigate(MovieScreens.GenresScreen.name){
                popUpTo(MovieScreens.GenresScreen.name){
                    inclusive = true
                }
            } }) {
                Text(text = "Refresh", textAlign = TextAlign.Center)
            }
        }
    }
    else{
        LazyVerticalGrid(modifier = Modifier.padding(horizontal = 16.dp), cells = GridCells.Fixed(2)){
            items(items = listOfGenre){ genre ->
                GenreRow(genre,navController)
            }
        }
    }
}

@Composable
fun GenreRow(genre: GenreX, navController: NavController) {
    val id = genre.id
    val title = genre.name
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(MovieScreens.MovieListScreen.name + "/${id}-${title}")
            }
            .fillMaxWidth()
            .height(72.dp)
            .padding(3.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxHeight()
                .fillMaxSize()) {
                Text(
                    text = genre.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }


        }

    }
}
