package com.teut2711.movies

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.teut2711.models.Movie
import org.teut2711.models.MovieDetails
import java.text.DateFormat
import java.util.Locale
import kotlin.math.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableMovieCard(movie: Movie, onClick:() -> Unit) {
    val imageLink = movie.backdropPath;
    OutlinedCard(modifier = Modifier
        .padding(8.dp, 8.dp)
        .fillMaxWidth(),
        colors= CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color.Gray),

        onClick=onClick,
        elevation = CardDefaults.cardElevation()){
        AsyncImage(
            model = if(imageLink != null)
                "https://image.tmdb.org/t/p/w500${imageLink}"
            else
                "https://img.freepik.com/free-photo/view-3d-cinema-elements_23-2150720822.jpg",
            contentDescription = movie.title,

            )
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(color = Color.Black, shape = CircleShape)
        ) {
            Text(
                text = round(movie.voteAverage*10).toString() +"%" ,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White, // Text color is set to white
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Text(text = if(movie.title !=null) movie.title else "Unspecified",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Blue,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        val formattedDate = movie.releaseDate?.let {
            DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(it)
        }.orEmpty()
        Text(text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )


    }

}


@Composable
fun <T> DataBox(label: String, value: T) {
    Box(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(3.dp))
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value?.toString() ?: "Not provided",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }
}


@Composable
fun MovieCard(movieDetails: MovieDetails) {
    val imageLink = movieDetails.posterPath;
    OutlinedCard(
        modifier = Modifier
            .padding(8.dp, 8.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation()
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(3.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = if (imageLink != null)
                    "https://image.tmdb.org/t/p/w500${imageLink}"
                else
                    "https://img.freepik.com/free-photo/view-3d-cinema-elements_23-2150720822.jpg",
                contentDescription = movieDetails.title,
                modifier = Modifier.fillMaxWidth()

            )
        }


        Row(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(3.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = if (movieDetails.title != null) movieDetails.title else "Unspecified",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Blue,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(9.dp)
            )
        }

        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(3.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            DataBox(
                value = movieDetails.overview,
                label = "Overview"
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp) // Adjust height as needed
            )
            DataBox(label = "Original Language", value = movieDetails.originalLanguage)
          val genreNames = if (movieDetails.genres != null && movieDetails.genres.isNotEmpty()) {
            movieDetails.genres.joinToString {it.name}
            } else {
                "No genres available"
            }
        DataBox(label = "Genre", value = genreNames)



        }


    }
}