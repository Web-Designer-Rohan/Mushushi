package com.rohan.mashashi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rohan.mashashi.data.local.entity.DailyLogs
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyLogsDao {
    @Query("SELECT * FROM daily_logs WHERE date = :date")
    fun getDailyLog(date: String): Flow<DailyLogs?>

    @Query("SELECT * FROM daily_logs WHERE date = :date")
    suspend fun getDailyLogSync(date: String): DailyLogs?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(dailyLog: DailyLogs)

    @Update
    suspend fun updateDailyLog(dailyLog: DailyLogs)

    @Query("SELECT * FROM daily_logs ORDER BY date DESC LIMIT 1")
    fun getLatestDailyLog(): Flow<DailyLogs?>

    @Query("SELECT SUM(pushupsCompleted) FROM daily_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getPushupsInRange(startDate: String, endDate: String): Int

    @Query("SELECT SUM(deepWorkMinutes) FROM daily_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getDeepWorkMinutesInRange(startDate: String, endDate: String): Int

    @Query("SELECT SUM(wisdomPoints) FROM daily_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getWisdomPointsInRange(startDate: String, endDate: String): Int

    @Query("SELECT SUM(disciplinePoints) FROM daily_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getDisciplinePointsInRange(startDate: String, endDate: String): Int

    @Query("SELECT COUNT(*) FROM daily_logs WHERE completed = 1 AND date >= :startDate AND date <= :endDate")
    suspend fun getCompletedDaysInRange(startDate: String, endDate: String): Int

    @Query("SELECT COUNT(*) FROM daily_logs WHERE date >= :startDate AND date <= :endDate")
    suspend fun getTotalDaysInRange(startDate: String, endDate: String): Int
}