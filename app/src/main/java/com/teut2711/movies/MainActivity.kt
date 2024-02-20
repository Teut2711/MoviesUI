package com.teut2711.movies

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import coil.compose.AsyncImage
import com.teut2711.movies.ui.theme.MoviesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.teut2711.models.Movie
import  org.teut2711.network.DefaultNetworkClient
import  org.teut2711.services.DefaultMovieService
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoviesTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                 MoviePreview()
                }
                }
            }
        }
    }


@Composable
fun MovieCard(movie:Movie) {
    val imageLink = movie.backdropPath;
    OutlinedCard(modifier = Modifier
        .padding(8.dp, 8.dp)
        .fillMaxWidth(),
      colors= CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
      ),
      elevation = CardDefaults.cardElevation()){

        AsyncImage(
          model= "https://image.tmdb.org/t/p/w500${imageLink}",
          contentDescription = movie.title,

      )
      Text(text = movie.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = androidx.compose.ui.graphics.Color.Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
      )


  }

}

class MovieViewModel : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var movies by mutableStateOf<List<Movie>>(emptyList())


     suspend fun fetchMovies() {

            try {
                val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjkzNGRiNDM5MGM2NThiOTI5YzBiYWU3ZGY2ODlmYiIsInN1YiI6IjY1Y2U3Mjc2ZGIxNTRmMDE0OTlkODFjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XR2B_l8H4IXNQGbugX6xkvNTdxN2-wbb56C52KsxSfQ"
                val networkClient = DefaultNetworkClient(bearerToken)
                val movieService = DefaultMovieService(networkClient)

                val fetchedMovies = withContext(Dispatchers.IO) {
                    movieService.getPopularMovies(1)
                }

                movies = fetchedMovies
                isLoading = false
            } catch (e: Exception) {
                errorMessage = e.message
                isLoading = false
            }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviePreview() {
    val viewModel: MovieViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.fetchMovies()
    }

    MoviesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Popular Movies",
                style = MaterialTheme.typography.displayMedium,
                color = androidx.compose.ui.graphics.Color.Blue,
                textAlign = TextAlign.Center
            )
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(viewModel.movies) { movie ->
                            MovieCard(movie)
                        }
                    }

        }
    }
}
