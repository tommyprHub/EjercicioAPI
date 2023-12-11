package com.example.ejercicioapi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ejercicioapi.adapter.MoviesAdapter
import com.example.ejercicioapi.api.ApiClient
import com.example.ejercicioapi.api.ApiService
import com.example.ejercicioapi.databinding.ActivityMainBinding
import com.example.ejercicioapi.response.MoviesListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val moviesAdapter by lazy { MoviesAdapter()}
    private val api : ApiService by lazy {
        ApiClient().getClient().create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //InitViews
        binding.apply {
            //show loading
            prgBarMovie.visibility = View.VISIBLE
            //Call movies api
            val callMoviesApi = api.getPopularMovie(1)

            callMoviesApi.enqueue(object : Callback<MoviesListResponse> {
                override fun onResponse(call: Call<MoviesListResponse>, response: Response<MoviesListResponse>) {
                    prgBarMovie.visibility = View.GONE
                    when (response.code()) {

                        in 200..299 -> {
                            Log.d("Response Code", " success messages : ${response.code()}")
                            response.body()?.let { itBody ->
                                itBody.results.let { itData ->
                                    if (itData.isNotEmpty()) {
                                        moviesAdapter.differ.submitList(itData)
                                        //Recycler
                                        rvMovie.apply {
                                            layoutManager = LinearLayoutManager(this@MainActivity)
                                            adapter = moviesAdapter
                                        }
                                    }
                                }
                            }
                        }
                        in 300..399 -> {
                            Log.d("Response Code", " Redirection messages : ${response.code()}")
                        }
                        in 400..499 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        in 500..599 -> {
                            Log.d("Response Code", " Server error responses : ${response.code()}")
                        }
                    }

                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovie.visibility = View.GONE
                    Log.e("onFailure", "Err : ${t.message}")
                }
            })
        }
    }
}