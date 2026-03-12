package com.rohan.mashashi.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rohan.mashashi.data.local.entity.UserStats
import com.rohan.mashashi.data.local.entity.DailyLogs
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStats>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsSync(): UserStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(userStats: UserStats)

    @Update
    suspend fun updateUserStats(userStats: UserStats)

    @Query("UPDATE user_stats SET totalXp = totalXp + :xp WHERE id = 1")
    suspend fun addXp(xp: Int)

    @Query("UPDATE user_stats SET strengthXp = strengthXp + :xp WHERE id = 1")
    suspend fun addStrengthXp(xp: Int)

    @Query("UPDATE user_stats SET focusXp = focusXp + :xp WHERE id = 1")
    suspend fun addFocusXp(xp: Int)

    @Query("UPDATE user_stats SET wisdomXp = wisdomXp + :xp WHERE id = 1")
    suspend fun addWisdomXp(xp: Int)

    @Query("UPDATE user_stats SET disciplineXp = disciplineXp + :xp WHERE id = 1")
    suspend fun addDisciplineXp(xp: Int)

    @Query("UPDATE user_stats SET currentStreak = currentStreak + 1 WHERE id = 1")
    suspend fun incrementStreak()

    @Query("UPDATE user_stats SET currentStreak = 0 WHERE id = 1")
    suspend fun resetStreak()

    @Query("UPDATE user_stats SET totalPushups = totalPushups + :pushups WHERE id = 1")
    suspend fun addPushups(pushups: Int)

    @Query("UPDATE user_stats SET totalDeepWorkMinutes = totalDeepWorkMinutes + :minutes WHERE id = 1")
    suspend fun addDeepWorkMinutes(minutes: Int)

    @Query("UPDATE user_stats SET totalResetsCompleted = totalResetsCompleted + 1 WHERE id = 1")
    suspend fun incrementResetsCompleted()

    @Query("UPDATE user_stats SET currentResetDay = currentResetDay + 1 WHERE id = 1")
    suspend fun incrementResetDay()

    @Query("UPDATE user_stats SET currentResetDay = 0 WHERE id = 1")
    suspend fun resetResetDay()

    @Query("UPDATE user_stats SET currentRank = :rank, lastRankUpTimestamp = :timestamp WHERE id = 1")
    suspend fun updateRank(rank: String, timestamp: Long)
}