package com.wrisband.pda.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wrisband.pda.data.models.Booking

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings")
    fun getAll(): List<Booking>

    @Query("SELECT * FROM bookings WHERE be_sn = :sn AND code = :code LIMIT 1")
    fun getByCodeAndSN(sn: String, code: String): Booking?

    @Insert
    fun insertAll(model: List<Booking>)

    @Delete
    fun delete(model: Booking)
}