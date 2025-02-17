package com.sasakiappstudio.sasakiflix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.sasakiappstudio.sasakiflix.database.FavoriteMovieDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val API_KEY = "eed05c05d23e1f884fae5af23db361d0"
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVerMais: MaterialButton
    private lateinit var searchView: SearchView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private val movieRepository = MovieRepository()
    private lateinit var db: FavoriteMovieDatabase
    private val allMovies = mutableListOf<Movie>()
    private val displayedMovies = mutableListOf<Movie>()
    private lateinit var adapter: MovieAdapter
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FavoriteMovieDatabase.getDatabase(this)

        setupDrawerAndToolbar()
        setupRecyclerView()
        setupNavigation()
        setupSearchView()

        btnVerMais = findViewById(R.id.btnVerMais)
        btnVerMais.setOnClickListener { loadMoreMovies() }
        searchView = findViewById(R.id.searchView)
        Log.d("DEBUG", "SearchView encontrada: ${searchView != null}")

        searchView.setOnClickListener {
            searchView.isIconified = false  // Expande a barra ao clicar nela
        }

        fetchMovies()
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.isIconified = false
        searchView.clearFocus()

        // ✅ Garante que a SearchView está visível
        searchView.visibility = View.VISIBLE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMovies(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMovies(newText)
                return true
            }
        })
    }

    private fun filterMovies(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            allMovies.filter { movie ->
                movie.title.contains(query, ignoreCase = true)
            }
        } else {
            allMovies
        }

        displayedMovies.clear()
        displayedMovies.addAll(filteredList)
        adapter.notifyDataSetChanged()
    }

    private fun setupDrawerAndToolbar() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = MovieAdapter(this, displayedMovies)
        recyclerView.adapter = adapter
    }

    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.nav_home -> fetchMovies()
                R.id.nav_favoritos -> fetchFavorites()
                R.id.nav_acao -> fetchMoviesByGenre(28)
                R.id.nav_aventura -> fetchMoviesByGenre(12)
                R.id.nav_terror -> fetchMoviesByGenre(27)
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun fetchMovies() {
        movieRepository.getPopularMovies(API_KEY) { movies ->
            if (movies != null) {
                allMovies.clear()
                allMovies.addAll(movies)
                currentPage = 0
                displayedMovies.clear()
                loadMoreMovies()
            } else {
                Log.e("API_ERROR", "Falha ao obter filmes")
            }
        }
    }

    private fun loadMoreMovies() {
        val startIndex = currentPage * 10
        val endIndex = minOf(startIndex + 10, allMovies.size)

        if (startIndex < allMovies.size) {
            displayedMovies.addAll(allMovies.subList(startIndex, endIndex))
            adapter.notifyDataSetChanged()
            currentPage++
        }

        btnVerMais.visibility = if (displayedMovies.size >= allMovies.size) View.GONE else View.VISIBLE
    }

    private fun fetchFavorites() {
        lifecycleScope.launch(Dispatchers.IO) {
            db.favoriteMovieDao().getAllFavorites().collect { favorites ->
                val movies = favorites.map {
                    Movie(it.id, it.title, it.posterPath ?: "", overview = "")
                }
                withContext(Dispatchers.Main) {
                    allMovies.clear()
                    allMovies.addAll(movies)
                    currentPage = 0
                    displayedMovies.clear()
                    loadMoreMovies()
                }
            }
        }
    }

    private fun fetchMoviesByGenre(genreId: Int) {
        movieRepository.getMoviesByGenre(API_KEY, genreId) { movies ->
            if (movies != null) {
                runOnUiThread {
                    allMovies.clear()
                    allMovies.addAll(movies)
                    currentPage = 0
                    displayedMovies.clear()
                    loadMoreMovies()
                }
            } else {
                Log.e("API_ERROR", "Falha ao obter filmes do gênero $genreId")
            }
        }
    }
}
