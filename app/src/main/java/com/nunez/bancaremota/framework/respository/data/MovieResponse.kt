package com.nunez.palcine.framework.respository.data

import com.squareup.moshi.Json

data class MovieResponse(
        @Json(name = "success") val successful: Boolean,
        @Json(name = "result") val list: List<MovieRaw>
)