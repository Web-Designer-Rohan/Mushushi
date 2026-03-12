package com.rohan.mashashi.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.rohan.mashashi.data.local.dao.UserStatsDao
import com.rohan.mashashi.data.local.dao.DailyLogsDao
import com.rohan.mashashi.data.local.entity.UserStats
import com.rohan.mashashi.data.local.entity.DailyLogs

@Database(
    entities = [UserStats::class, DailyLogs::class],
    version = 1,
    exportSchema = false
)
abstract class MashashiDatabase : RoomDatabase() {
    abstract fun userStatsDao(): UserStatsDao
    abstract fun dailyLogsDao(): DailyLogsDao

    companion object {
        @Volatile
        private var INSTANCE: MashashiDatabase? = null

        fun getDatabase(context: Context): MashashiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MashashiDatabase::class.java,
                    "mashashi_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}