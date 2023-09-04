package com.wrisband.pda.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity("bookings")
data class Booking(
    @PrimaryKey
    @SerializedName("id")
    val booking_id: Int,

    @ColumnInfo
    @SerializedName("code")
    val code: String? = null,

    @Embedded(prefix = "bu_")
    @SerializedName("user")
    var user: User? = null,

    @Embedded(prefix = "be_")
    @SerializedName("event")
    var event: Event? = null
)
