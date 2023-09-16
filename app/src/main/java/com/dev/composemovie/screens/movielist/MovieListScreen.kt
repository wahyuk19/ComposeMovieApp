package com.dev.composemovie.screens.movielist

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.dev.composemovie.BuildConfig
import com.dev.composemovie.components.MovieAppBar
import com.dev.composemovie.model.Result
import com.dev.composemovie.navigation.MovieScreens
import java.util.Locale

@Composable
fun MovieListScreen(
    navController: NavController,
    genreId: String,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val title = genreId.substringAfter("-")
    Scaffold(topBar = {
        MovieAppBar(
            title = title,
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showReviews = false,
            items = null
        ) {
            navController.navigate(MovieScreens.GenresScreen.name)
        }
    }) {
        Surface {
            Column {
                MovieList(navController = navController, genreId)
            }
            BackHandler {
                navController.navigate(MovieScreens.GenresScreen.name)
            }
        }
    }
}

@Composable
fun MovieList(
    navController: NavController,
    genreId: String,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val id = genreId.substringBefore("-")
    val title = genreId.substringAfter("-")
    val movieItems: LazyPagingItems<Result> =
        viewModel.getMoviesByGenre(id.toInt()).collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, contentPadding = PaddingValues(16.dp)
    ) {
        items(movieItems.itemCount) { index ->
            Card(modifier = Modifier
                .clickable {
                    navController.navigate(MovieScreens.MovieDetailScreen.name + "/${movieItems[index]?.id}-${movieItems[index]?.title}")
                }
                .fillMaxWidth()
                .height(72.dp)
                .padding(3.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 7.dp) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    val imageUrl = "${BuildConfig.IMAGE_URL}${movieItems[index]?.poster_path}"
                    Image(
                        painter = rememberImagePainter(data = imageUrl),
                        contentDescription = "movie image",
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .padding(end = 4.dp),
                    )

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                        Text(
                            text = movieItems[index]?.title.toString().replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                        )
                    }


                }

            }
        }
        movieItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = movieItems.loadState.refresh as LoadState.Error
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Error retrieve data\n\n${error.error.localizedMessage}",
                                textAlign = TextAlign.Center
                            )
                            Button(onClick = {
                                navController.navigate(MovieScreens.MovieListScreen.name + "/${id}-${title}") {
                                    popUpTo(MovieScreens.MovieListScreen.name+ "/${id}-${title}") {
                                        inclusive = true
                                    }
                                }
                            }) {
                                Text(text = "Refresh", textAlign = TextAlign.Center)
                            }
                        }
//                        Toast.makeText(LocalContext.current,"Load Movies Error : ${error.error.localizedMessage}",Toast.LENGTH_SHORT).show()
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = movieItems.loadState.append as LoadState.Error
                    item {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .padding(horizontal = 16.dp),
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            Text(text = "Error retrieve data\n\n${error.error.localizedMessage}", textAlign = TextAlign.Center)
//                            Button(onClick = {navController.navigate(MovieScreens.MovieListScreen.name + "/${id}-${title}"){
//                                popUpTo(MovieScreens.MovieListScreen.name){
//                                    inclusive = true
//                                }
//                            } }) {
//                                Text(text = "Refresh", textAlign = TextAlign.Center)
//                            }
//                        }
                        Toast.makeText(
                            LocalContext.current,
                            "Load error : ${error.error.localizedMessage} \nplease refresh page",
                            Toast.LENGTH_SHORT
                        ).show()
                        Button(onClick = {
                            navController.navigate(MovieScreens.MovieListScreen.name + "/${id}-${title}") {
                                popUpTo(MovieScreens.MovieListScreen.name+ "/${id}-${title}") {
                                    inclusive = true
                                }
                            }
                        }) {
                            Text(text = "Refresh", textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

    }
}
