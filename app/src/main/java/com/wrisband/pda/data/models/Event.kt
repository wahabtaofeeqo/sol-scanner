package com.wrisband.pda.data.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id")
    val event_id: String,

    @SerializedName("sn")
    val sn: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("date")
    val date: String
)
