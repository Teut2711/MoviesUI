package com.teut2711.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teut2711.movies.ui.theme.MoviesTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        val delta = available.y
                         return Offset.Zero
                    }
                }
            }
            val viewModel: MovieViewModel = viewModel()

            MoviesTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavHost( navController = navController, startDestination = "popular") {
                        composable("latest") { LatestMoviePreview(navController, viewModel ) }
                        composable("popular") { PopularMoviesPreview(navController,nestedScrollConnection, viewModel) }
                        composable("detail/{movieId}") { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                            MovieDetailsPreview(navController,viewModel, movieId)
                        }

                    }

                }
                }
            }
        }
    }


@Composable
fun MovieDetailsPreview(    navController: NavHostController = rememberNavController(),
                            viewModel: MovieViewModel, movieId: Int) {

    LaunchedEffect(Unit) {

        viewModel.fetchMovieDetails(movieId)
    }

    MoviesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "${viewModel.movie}",
                style = MaterialTheme.typography.displayMedium,
                color = androidx.compose.ui.graphics.Color.Blue,
                textAlign = TextAlign.Center
            )
            MovieCard(viewModel.movie)

        }

    }

}

@Preview
@Composable
fun MoviePreview() {
    LatestMoviePreview(rememberNavController(), viewModel())
}

@Composable
fun LatestMoviePreview(
    navController: NavHostController = rememberNavController(), viewModel: MovieViewModel) {

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.fetchLatestMovie()
            delay(60000) // 60 seconds = 1 minute
        }
    }

    MoviesTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Latest Movie",
                style = MaterialTheme.typography.displayMedium,
                color = androidx.compose.ui.graphics.Color.Blue,
                textAlign = TextAlign.Center
            )
            Button(onClick = { navController.navigate("popular")}) {
                Text("Go to Popular Movies")
            }
            MovieCard(viewModel.movie)

        }

    }
}



@Composable
fun PopularMoviesPreview(
    navController: NavHostController = rememberNavController(),
    nestedScrollConnection: NestedScrollConnection,
    viewModel: MovieViewModel
) {
    val page by remember {
        mutableIntStateOf(1)
    }
    LaunchedEffect(Unit) {
        viewModel.fetchPopularMovies(page)
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
            LazyVerticalGrid(columns = GridCells.Fixed(3),
                modifier = Modifier.nestedScroll(nestedScrollConnection)
                ) {
                items(viewModel.movies) { movie ->
                    ClickableMovieCard(movie = movie) {
                        navController.navigate("detail/${movie.id}")
                    }
                    }

        }
    }
}}



