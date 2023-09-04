package com.wrisband.pda.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wrisband.pda.data.models.Booking
import com.wrisband.pda.data.models.Scanned

@Database(entities = [Scanned::class, Booking::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scannedDao(): ScannedDao
    abstract fun bookingDao(): BookingDao

    companion object {
        const val DATABASE_NAME = "com.wristband.pda.db"
    }
}