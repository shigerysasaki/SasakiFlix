package com.sasakiappstudio.sasakiflix

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sasakiappstudio.sasakiflix.database.FavoriteMovie
import com.sasakiappstudio.sasakiflix.database.FavoriteMovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.movieTitle)
        val poster: ImageView = view.findViewById(R.id.moviePoster)
        val btnFavorite: ImageView = view.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        val db = FavoriteMovieDatabase.getDatabase(holder.itemView.context)
        val favoriteDao = db.favoriteMovieDao()

        holder.title.text = movie.title

        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath ?: ""}"
        Glide.with(holder.poster.context).load(posterUrl).into(holder.poster)

        CoroutineScope(Dispatchers.IO).launch {
            val isFav = favoriteDao.isFavorite(movie.id) > 0 // ✅ Conversão correta de Int para Boolean
            withContext(Dispatchers.Main) {
                holder.btnFavorite.setImageResource(
                    if (isFav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
            }
        }

        holder.btnFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val isFav = favoriteDao.isFavorite(movie.id) > 0
                if (isFav) {
                    favoriteDao.removeFavorite(FavoriteMovie(movie.id, movie.title, movie.posterPath ?: ""))
                } else {
                    favoriteDao.addFavorite(FavoriteMovie(movie.id, movie.title, movie.posterPath ?: ""))
                }
                withContext(Dispatchers.Main) {
                    notifyItemChanged(position)
                }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MovieDetailActivity::class.java)
            intent.putExtra("movie", movie)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = movies.size
}