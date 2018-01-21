package com.nunez.palcine.framework.respository.data

import com.squareup.moshi.Json

data class MovieRaw(
        val id: Int,
        val title: String,
        @Json(name = "original_title") val originalTitle: String,
        val premiere: Boolean,
        val overview: String,
        val trailers: List<String>,
        val theaters: List<TheaterRaw>,
        val images: ImagesRaw,
        val genres: List<GenreRaw>,
        val score: String)

data class ImagesRaw(
        val tiny: String,
        val small: String,
        val medium: String,
        val big: String,
        val giant: String)

data class GenreRaw(val name: String)