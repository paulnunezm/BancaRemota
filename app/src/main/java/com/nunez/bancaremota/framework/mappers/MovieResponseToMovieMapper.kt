package com.nunez.palcine.framework.mappers

import android.util.Log
import com.nunez.palcine.framework.respository.data.MovieResponse
import com.nunez.palcine.screens.movieList.Movie
import io.reactivex.functions.Function


class MovieResponseToMovieMapper : Function<MovieResponse, List<Movie>> {
    override fun apply(t: MovieResponse): List<Movie> {
        val movies = ArrayList<Movie>()
        Log.v("MovieResponse", "${t.list.size}")
        t.list.forEach {
            //            val genres = ArrayList<Genre>(it.genres.size)
//            it.genres.forEach {
//                genres.add(Genre(it.name))
//            }
            movies.add(
                    Movie(it.id,
                            it.title,
                            it.images.small,
                            emptyList(),
                            it.score)
            )
        }
        return movies.toList()
    }
}