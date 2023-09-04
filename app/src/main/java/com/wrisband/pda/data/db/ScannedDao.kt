package com.wrisband.pda.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wrisband.pda.data.models.Scanned

@Dao
interface ScannedDao {

    @Query("SELECT * FROM scanned")
    fun getAll(): List<Scanned>

    @Query("SELECT * FROM scanned WHERE date = :date")
    fun getByDate(date: String): Scanned?

    @Query("UPDATE scanned SET count = :count WHERE id = :id")
    fun updateCount(count: Int, id: Int)

    @Insert
    fun insert(model: Scanned)

    @Delete
    fun delete(model: Scanned)
}