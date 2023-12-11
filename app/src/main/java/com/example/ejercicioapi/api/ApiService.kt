package com.example.ejercicioapi.api

import com.example.ejercicioapi.DetailesMovieActivity
import com.example.ejercicioapi.response.MoviesListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //    https://api.themoviedb.org/3/movie/550?api_key=***
    //    https://api.themoviedb.org/3/movie/popular?api_key=***
    //    https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Call<DetailesMovieActivity>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Call<MoviesListResponse>

}