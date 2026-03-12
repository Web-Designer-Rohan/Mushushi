package com.rohan.mashashi.domain.usecase

import com.rohan.mashashi.data.local.MashashiDatabase
import com.rohan.mashashi.domain.logic.RankCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class AddExperience(
    private val database: MashashiDatabase
) {
    
    // XP Values per action
    private val XP_PER_PUSHUP = 5
    private val XP_PER_DEEP_WORK_MINUTE = 10
    private val XP_PER_90_DAY_RESET = 100

    /**
     * Add strength XP for pushups
     */
    suspend fun addPushupXp(pushupCount: Int): Flow<ExperienceResult> = flow {
        val userDao = database.userStatsDao()
        val dailyDao = database.dailyLogsDao()

        // Get current stats
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()

        // Calculate XP
        val xpGained = pushupCount * XP_PER_PUSHUP
        val newStrengthXp = currentStats.strengthXp + xpGained
        val newTotalXp = currentStats.totalXp + xpGained

        // Update user stats
        val updatedStats = currentStats.copy(
            totalXp = newTotalXp,
            strengthXp = newStrengthXp,
            totalPushups = currentStats.totalPushups + pushupCount
        )

        userDao.updateUserStats(updatedStats)

        // Update daily logs
        val date = getCurrentDate()
        val dailyLog = dailyDao.getDailyLogSync(date) ?: createDefaultDailyLog(date)
        val updatedDailyLog = dailyLog.copy(
            pushupsCompleted = dailyLog.pushupsCompleted + pushupCount,
            disciplinePoints = dailyLog.disciplinePoints + (xpGained / 5) // Partial discipline XP
        )
        dailyDao.updateDailyLog(updatedDailyLog)

        // Calculate new rank
        val newRank = RankCalculator.calculateRank(newTotalXp)

        emit(
            ExperienceResult(
                xpGained = xpGained,
                totalXp = newTotalXp,
                strengthXp = newStrengthXp,
                newRank = newRank,
                rankUp = newRank != currentStats.currentRank,
                pushups = pushupCount
            )
        )
    }

    /**
     * Add focus XP for deep work
     */
    suspend fun addDeepWorkXp(minutes: Int): Flow<ExperienceResult> = flow {
        val userDao = database.userStatsDao()
        val dailyDao = database.dailyLogsDao()

        // Get current stats
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()

        // Calculate XP
        val xpGained = minutes * XP_PER_DEEP_WORK_MINUTE
        val newFocusXp = currentStats.focusXp + xpGained
        val newTotalXp = currentStats.totalXp + xpGained

        // Update user stats
        val updatedStats = currentStats.copy(
            totalXp = newTotalXp,
            focusXp = newFocusXp,
            totalDeepWorkMinutes = currentStats.totalDeepWorkMinutes + minutes
        )

        userDao.updateUserStats(updatedStats)

        // Update daily logs
        val date = getCurrentDate()
        val dailyLog = dailyDao.getDailyLogSync(date) ?: createDefaultDailyLog(date)
        val updatedDailyLog = dailyLog.copy(
            deepWorkMinutes = dailyLog.deepWorkMinutes + minutes,
            disciplinePoints = dailyLog.disciplinePoints + (xpGained / 10) // Partial discipline XP
        )
        dailyDao.updateDailyLog(updatedDailyLog)

        // Calculate new rank
        val newRank = RankCalculator.calculateRank(newTotalXp)

        emit(
            ExperienceResult(
                xpGained = xpGained,
                totalXp = newTotalXp,
                focusXp = newFocusXp,
                newRank = newRank,
                rankUp = newRank != currentStats.currentRank,
                deepWorkMinutes = minutes
            )
        )
    }

    /**
     * Complete a 90-day reset task
     */
    suspend fun completeResetTask(): Flow<ExperienceResult> = flow {
        val userDao = database.userStatsDao()
        val dailyDao = database.dailyLogsDao()

        // Get current stats
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()

        // Calculate XP
        val xpGained = XP_PER_90_DAY_RESET
        val newDisciplineXp = currentStats.disciplineXp + xpGained
        val newTotalXp = currentStats.totalXp + xpGained

        // Update user stats
        val updatedStats = currentStats.copy(
            totalXp = newTotalXp,
            disciplineXp = newDisciplineXp,
            totalResetsCompleted = currentStats.totalResetsCompleted + 1,
            currentResetDay = currentStats.currentResetDay + 1
        )

        userDao.updateUserStats(updatedStats)

        // Update daily logs
        val date = getCurrentDate()
        val dailyLog = dailyDao.getDailyLogSync(date) ?: createDefaultDailyLog(date)
        val updatedDailyLog = dailyLog.copy(
            disciplinePoints = dailyLog.disciplinePoints + xpGained,
            completed = true
        )
        dailyDao.updateDailyLog(updatedDailyLog)

        // Calculate new rank
        val newRank = RankCalculator.calculateRank(newTotalXp)

        emit(
            ExperienceResult(
                xpGained = xpGained,
                totalXp = newTotalXp,
                disciplineXp = newDisciplineXp,
                newRank = newRank,
                rankUp = newRank != currentStats.currentRank,
                resetCompleted = true
            )
        )
    }

    /**
     * Get current streak
     */
    suspend fun getCurrentStreak(): Flow<Int> = flow {
        val userDao = database.userStatsDao()
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()
        emit(currentStats.currentStreak)
    }

    /**
     * Get 90-day reset progress
     */
    suspend fun getResetProgress(): Flow<Int> = flow {
        val userDao = database.userStatsDao()
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()
        emit(currentStats.currentResetDay)
    }

    /**
     * Get rank information
     */
    suspend fun getRankInfo(): Flow<RankInfo> = flow {
        val userDao = database.userStatsDao()
        val currentStats = userDao.getUserStatsSync() ?: createDefaultStats()
        
        val rank = RankCalculator.calculateRank(currentStats.totalXp)
        val progress = RankCalculator.getRankProgress(currentStats.totalXp)
        
        emit(
            RankInfo(
                rank = rank,
                progress = progress,
                totalXp = currentStats.totalXp,
                strengthXp = currentStats.strengthXp,
                focusXp = currentStats.focusXp,
                wisdomXp = currentStats.wisdomXp,
                disciplineXp = currentStats.disciplineXp
            )
        )
    }

    private fun createDefaultStats(): UserStats {
        return UserStats(
            id = 1,
            level = 1,
            totalXp = 0,
            strengthXp = 0,
            focusXp = 0,
            wisdomXp = 0,
            disciplineXp = 0,
            currentRank = "E",
            lastRankUpTimestamp = 0L,
            currentStreak = 0,
            longestStreak = 0,
            totalPushups = 0,
            totalDeepWorkMinutes = 0,
            totalResetsCompleted = 0,
            currentResetDay = 0
        )
    }

    private fun createDefaultDailyLog(date: String): DailyLogs {
        return DailyLogs(
            id = 0,
            date = date,
            pushupsCompleted = 0,
            deepWorkMinutes = 0,
            wisdomPoints = 0,
            disciplinePoints = 0,
            streak = 0,
            completed = false
        )
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    data class ExperienceResult(
        val xpGained: Int,
        val totalXp: Int,
        val strengthXp: Int = 0,
        val focusXp: Int = 0,
        val wisdomXp: Int = 0,
        val disciplineXp: Int = 0,
        val newRank: String,
        val rankUp: Boolean,
        val pushups: Int = 0,
        val deepWorkMinutes: Int = 0,
        val wisdomPoints: Int = 0,
        val resetCompleted: Boolean = false
    )

    data class RankInfo(
        val rank: String,
        val progress: Int,
        val totalXp: Int,
        val strengthXp: Int,
        val focusXp: Int,
        val wisdomXp: Int,
        val disciplineXp: Int
    )
}