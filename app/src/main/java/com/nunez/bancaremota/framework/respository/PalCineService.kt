package com.nunez.palcine.framework.respository

import com.nunez.palcine.framework.respository.data.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET

interface PalCineService {

    @GET(Endpoints.MOVIES)
    fun getMovies(): Single<MovieResponse>

}