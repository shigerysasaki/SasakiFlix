package com.sasakiappstudio.sasakiflix

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {

    private val api = RetrofitClient.instance.create(MovieApi::class.java)

    // Buscar filmes populares
    fun getPopularMovies(apiKey: String, callback: (List<Movie>?) -> Unit) {
        api.getPopularMovies(apiKey).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.results)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

    // Buscar filmes por gênero
    fun getMoviesByGenre(apiKey: String, genreId: Int, callback: (List<Movie>?) -> Unit) {
        api.getMoviesByGenre(apiKey, genreId).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.results)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
