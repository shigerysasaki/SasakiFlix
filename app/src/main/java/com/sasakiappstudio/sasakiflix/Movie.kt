    package com.sasakiappstudio.sasakiflix

    import android.os.Parcelable
    import com.google.gson.annotations.SerializedName
    import kotlinx.parcelize.Parcelize

    @Parcelize
    data class Movie(
        val id: Int,
        val title: String,
        @SerializedName("poster_path") val posterPath: String?, // ✅ Permitir valores nulos
        @SerializedName("overview") val overview: String
    ) : Parcelable

    data class MovieResponse(
        val results: List<Movie>
    )
