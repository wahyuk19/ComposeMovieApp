package com.dev.composemovie.screens.reviews

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.composemovie.components.MovieAppBar
import com.dev.composemovie.model.ResultX
import com.dev.composemovie.navigation.MovieScreens
import com.dev.composemovie.utils.convertDateTimeFormat
import java.util.Locale

@Composable
fun ReviewsScreen(
    navController: NavController,
    movieId: String,
    viewModel: ReviewsViewModel = hiltViewModel()
) {
    Log.d("TAG", "ReviewsScreen: movieID $movieId")
    val title = movieId.substringAfter("-")
    Scaffold(topBar = {
        MovieAppBar(
            title = "Reviews - $title",
            icon = Icons.Default.ArrowBack,
            navController = navController,
            showReviews = false,
            items = null
        ) {
            navController.navigate(MovieScreens.MovieDetailScreen.name + "/$movieId")
        }
    }) {
        Surface {
            Column {
                MovieReviews(navController = navController, movieId, viewModel)
            }
            BackHandler {
                navController.navigate(MovieScreens.MovieDetailScreen.name + "/$movieId")
            }
        }
    }
}

@Composable
fun MovieReviews(navController: NavController, movieId: String, viewModel: ReviewsViewModel) {
    val id = movieId.substringBefore("-")
    val reviewItems: LazyPagingItems<ResultX> =
        viewModel.getMovieReviewById(id.toInt()).collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, contentPadding = PaddingValues(16.dp)) {
        items(reviewItems.itemCount) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 7.dp
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = reviewItems[index]?.author.toString().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        },
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val dateTime = convertDateTimeFormat(reviewItems[index]?.created_at.toString())
                    Text(
                        text = dateTime
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = reviewItems[index]?.content.toString())
                }


            }
        }
        reviewItems.apply {
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
                    val error = reviewItems.loadState.refresh as LoadState.Error
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
                                navController.navigate(MovieScreens.ReviewsScreen.name + "/${movieId}") {
                                    popUpTo(MovieScreens.ReviewsScreen.name+ "/${movieId}") {
                                        inclusive = true
                                    }
                                }
                            }) {
                                Text(text = "Refresh", textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Row(horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = reviewItems.loadState.append as LoadState.Error
                    item {
                        Toast.makeText(
                            LocalContext.current,
                            "Load error : ${error.error.localizedMessage} \nplease refresh page",
                            Toast.LENGTH_SHORT
                        ).show()
                        Button(onClick = {
                            navController.navigate(MovieScreens.ReviewsScreen.name + "/${movieId}") {
                                popUpTo(MovieScreens.ReviewsScreen.name + "/${movieId}") {
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