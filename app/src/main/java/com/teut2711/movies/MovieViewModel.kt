package com.teut2711.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.teut2711.models.Movie
import org.teut2711.models.MovieDetails
import org.teut2711.network.DefaultNetworkClient
import org.teut2711.services.DefaultMovieService

class MovieViewModel : ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)
    var movies by mutableStateOf<List<Movie>>(emptyList())
    var movie by mutableStateOf<MovieDetails>(MovieDetails())


    suspend fun fetchPopularMovies(page:Int){

        try {
            val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjkzNGRiNDM5MGM2NThiOTI5YzBiYWU3ZGY2ODlmYiIsInN1YiI6IjY1Y2U3Mjc2ZGIxNTRmMDE0OTlkODFjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XR2B_l8H4IXNQGbugX6xkvNTdxN2-wbb56C52KsxSfQ"
            val networkClient = DefaultNetworkClient(bearerToken)
            val movieService = DefaultMovieService(networkClient)

            val fetchedMovies = withContext(Dispatchers.IO) {
                movieService.getPopularMovies(page)
            }

            movies = fetchedMovies
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
        }
    }

    suspend fun fetchLatestMovie() {

        try {
            val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjkzNGRiNDM5MGM2NThiOTI5YzBiYWU3ZGY2ODlmYiIsInN1YiI6IjY1Y2U3Mjc2ZGIxNTRmMDE0OTlkODFjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XR2B_l8H4IXNQGbugX6xkvNTdxN2-wbb56C52KsxSfQ"
            val networkClient = DefaultNetworkClient(bearerToken)
            val movieService = DefaultMovieService(networkClient)

            val fetchedMovie = withContext(Dispatchers.IO) {
                movieService.latestMovie
            }

            movie = fetchedMovie
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
        }
    }
    suspend fun fetchMovieDetails(id:Int) {

        try {
            val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjkzNGRiNDM5MGM2NThiOTI5YzBiYWU3ZGY2ODlmYiIsInN1YiI6IjY1Y2U3Mjc2ZGIxNTRmMDE0OTlkODFjYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XR2B_l8H4IXNQGbugX6xkvNTdxN2-wbb56C52KsxSfQ"
            val networkClient = DefaultNetworkClient(bearerToken)
            val movieService = DefaultMovieService(networkClient)

            val fetchedMovie = withContext(Dispatchers.IO) {
                movieService.getMovieDetails(id)
            }

            movie = fetchedMovie
            isLoading = false
        } catch (e: Exception) {
            errorMessage = e.message
            isLoading = false
        }
    }
}
