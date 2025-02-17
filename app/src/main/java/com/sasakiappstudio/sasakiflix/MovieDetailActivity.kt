package com.sasakiappstudio.sasakiflix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Inicializa a Toolbar corretamente
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhes do Filme"

        // Inicializa os componentes da UI
        val movieTitleDetail: TextView = findViewById(R.id.movieTitleDetail)
        val moviePosterDetail: ImageView = findViewById(R.id.moviePosterDetail)
        val movieOverview: TextView = findViewById(R.id.movieOverview)

        // Receber dados do Intent
        val movie = intent.getParcelableExtra<Movie>("movie")

        // Atualizar os detalhes do filme
        movie?.let {
            movieTitleDetail.text = it.title
            movieOverview.text = it.overview

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${it.posterPath}")
                .into(moviePosterDetail)
        }

        // Botão de voltar funcionando corretamente
        toolbar.setNavigationOnClickListener { finish() }
    }
}
