package com.wrisband.pda.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("scanned")
data class Scanned(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val count: Int,
    val date: String
)
