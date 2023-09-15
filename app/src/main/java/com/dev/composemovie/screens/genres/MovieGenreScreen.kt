package com.dev.composemovie.screens.genres

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dev.composemovie.components.MovieAppBar
import com.dev.composemovie.model.GenreX
import com.dev.composemovie.navigation.MovieScreens
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MovieGenreScreen(navController: NavController,viewModel: MovieGenreViewModel = hiltViewModel()){
    Scaffold(topBar = {
        MovieAppBar(title = "Select a Genre", navController = navController)
    }) {
        Surface {
            Column {
                GenreList(navController = navController)
            }
        }
    }
}

@Composable
fun GenreList(navController: NavController,viewModel: MovieGenreViewModel = hiltViewModel()) {
    val listOfGenre = viewModel.list
    if(viewModel.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)){
            items(items = listOfGenre){ genre ->
                GenreRow(genre,navController)
            }
        }
    }
}

@Composable
fun GenreRow(genre: GenreX, navController: NavController) {
    val id = genre.id
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(MovieScreens.MovieListScreen.name + "/${id}")
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

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = genre.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    textAlign = TextAlign.Center
                )
            }


        }

    }
}
