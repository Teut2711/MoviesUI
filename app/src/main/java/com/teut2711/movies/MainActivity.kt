package com.teut2711.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                    color = Color.White,
                ) {
                    NavHost( navController = navController, startDestination = "latest") {
                        composable("latest") { LatestMovieScreen(navController, viewModel ) }
                        composable("popular") { PopularMoviesScreen(navController,nestedScrollConnection, viewModel) }
                        composable(
                            route = "detail/{movieId}",
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) {
                             backStackEntry ->
    val movieId = backStackEntry.arguments!!.getInt("movieId")
    MovieDetailsScreen(navController,viewModel, movieId)



                        }


                                       }

                }
                }
            }
        }
    }


@Composable
fun MovieDetailsScreen(navController: NavHostController = rememberNavController(),
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
                text =  if (viewModel.movieDetails.title != null) viewModel.movieDetails.title else "Unspecified",
                style = MaterialTheme.typography.displayMedium,
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
            Row {
                Button(onClick = { navController.navigate("popular")}) {
                    Text("Go to Popular Movies")
                }
                Button(onClick = { navController.navigate("latest")}) {
                    Text("Go to latest Movie")
                }

            }
            MovieCard(viewModel.movieDetails)

        }

    }

}



@Composable
fun LatestMovieScreen(
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
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
            Button(onClick = { navController.navigate("popular")}) {
                Text("Go to Popular Movies")
            }
            MovieCard(viewModel.movieLatest)

        }

    }
}



@Composable
fun PopularMoviesScreen(
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
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
            Button(onClick = { navController.navigate("latest")}) {
                Text("Go to latest Movie")
            }

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



