package com.dicoding.tourismapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListTourismResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    var message: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("places")
    val places: List<TourismResponse>
)