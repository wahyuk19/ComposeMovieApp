package com.dev.composemovie.screens.details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dev.composemovie.BuildConfig
import com.dev.composemovie.components.MovieAppBar
import com.dev.composemovie.data.Resource
import com.dev.composemovie.model.MovieDetails
import com.dev.composemovie.model.Videos
import com.dev.composemovie.navigation.MovieScreens
import com.dev.composemovie.utils.convertDateFormat
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.Locale

@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: String,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val title = movieId.substringAfter("-")
    val id = movieId.substringBefore("-")
    Scaffold(topBar = {
        MovieAppBar(
            title =
            title, icon = Icons.Default.ArrowBack, showReviews = true, navController = navController,items = movieId
        ) {
            navController.navigate(MovieScreens.MovieListScreen.name + "/${id}-${title}")
        }
    }) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val movieInfo =
                    produceState<Resource<MovieDetails>>(initialValue = Resource.Loading()) {
                        value = viewModel.getMovieDetails(id.toInt())
                    }.value

                if (movieInfo.data == null) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if(movieInfo.message == null){
                                CircularProgressIndicator()
                                Toast.makeText(LocalContext.current,"retrieving data..",Toast.LENGTH_SHORT).show()
                            }else{
                                Text(text = "Error retrieve data\n\n${movieInfo.message}", textAlign = TextAlign.Center)
                                Button(onClick = {
                                    navController.navigate(MovieScreens.MovieDetailScreen.name + "/$movieId") {
                                        popUpTo(MovieScreens.MovieDetailScreen.name + "/$movieId"){
                                            inclusive = true
                                        }
                                    } }) {
                                    Text(text = "Refresh", textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        ) {
                        ShowMovieDetails(movieInfo, viewModel)
                    }
                }

            }
        }
    }
}

@Composable
fun ShowMovieDetails(movieInfo: Resource<MovieDetails>, viewModel: MovieDetailViewModel) {
    val imageUrl = "${BuildConfig.IMAGE_URL}${movieInfo.data?.poster_path}"
    Row {
        Image(
            painter = rememberImagePainter(data = imageUrl),
            contentDescription = "Movie Image",
            modifier = Modifier
                .width(200.dp)
                .height(250.dp)
                .padding(1.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = movieInfo.data?.tagline.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Release date : ${
                    convertDateFormat(movieInfo.data?.release_date.toString())}",
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Language : ${
                    movieInfo.data?.original_language.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                }",
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Runtime : ${
                    movieInfo.data?.runtime.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                } Minutes",
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
    Spacer(modifier = Modifier.padding(vertical = 16.dp))
    Row(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = movieInfo.data?.overview.toString()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            overflow = TextOverflow.Ellipsis,
        )
    }
    val getVideos = produceState<Resource<Videos>>(initialValue = Resource.Loading()) {
        value = viewModel.getVideo(movieInfo.data?.id.toString().toInt())
    }.value
    val videoId: String
    if (getVideos.data != null) {
        for (i in 0 until getVideos.data.results.size) {
            if (getVideos.data.results[i].type == "Trailer") {
                videoId = getVideos.data.results[i].key
                Log.d("TAG", "ShowMovieDetails: video id $videoId")
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AndroidView(factory = {
                        val view = YouTubePlayerView(it)
                        view.addYouTubePlayerListener(
                            object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    youTubePlayer.loadVideo(videoId, 0f)
                                }
                            }
                        )
                        view
                    })
                }
                break
            }
        }
    }
}
