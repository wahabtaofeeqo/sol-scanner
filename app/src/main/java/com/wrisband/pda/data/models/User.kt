package com.wrisband.pda.data.models

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val user_id: Int,

    @SerializedName("name")
    val name: String
)
