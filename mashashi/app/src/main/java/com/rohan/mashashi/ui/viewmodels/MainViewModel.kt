package com.rohan.mashashi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.mashashi.data.local.MashashiDatabase
import com.rohan.mashashi.domain.usecase.AddExperience
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: MashashiDatabase
) : ViewModel() {
    
    private val addExperience = AddExperience(database)
    
    // User stats flow
    val userStats: Flow<UserStats> = database.userStatsDao()
        .getUserStats()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserStats(
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
        )

    // Rank information
    val rankInfo: Flow<RankInfo> = userStats
        .map { stats ->
            val rank = RankCalculator.calculateRank(stats.totalXp)
            val progress = RankCalculator.getRankProgress(stats.totalXp)
            RankInfo(rank, progress, stats.totalXp, stats.strengthXp, stats.focusXp, stats.wisdomXp, stats.disciplineXp)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RankInfo("E", 0, 0, 0, 0, 0, 0)
        )

    // Streak information
    val streakInfo: Flow<StreakInfo> = userStats
        .map { stats ->
            StreakInfo(stats.currentStreak, stats.longestStreak, stats.currentResetDay)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StreakInfo(0, 0, 0)
        )

    // Actions
    fun addPushupXp(pushupCount: Int) {
        viewModelScope.launch {
            addExperience.addPushupXp(pushupCount)
                .collect { result ->
                    // Handle result if needed
                    Log.d("MainViewModel", "Added ${result.xpGained} XP from pushups")
                }
        }
    }

    fun addDeepWorkXp(minutes: Int) {
        viewModelScope.launch {
            addExperience.addDeepWorkXp(minutes)
                .collect { result ->
                    Log.d("MainViewModel", "Added ${result.xpGained} XP from deep work")
                }
        }
    }

    fun completeResetTask() {
        viewModelScope.launch {
            addExperience.completeResetTask()
                .collect { result ->
                    Log.d("MainViewModel", "Completed reset task, gained ${result.xpGained} XP")
                }
        }
    }

    // Data classes
    data class RankInfo(
        val rank: String,
        val progress: Int,
        val totalXp: Int,
        val strengthXp: Int,
        val focusXp: Int,
        val wisdomXp: Int,
        val disciplineXp: Int
    )

    data class StreakInfo(
        val currentStreak: Int,
        val longestStreak: Int,
        val currentResetDay: Int
    )

    data class UserStats(
        val id: Int,
        val level: Int,
        val totalXp: Int,
        val strengthXp: Int,
        val focusXp: Int,
        val wisdomXp: Int,
        val disciplineXp: Int,
        val currentRank: String,
        val lastRankUpTimestamp: Long,
        val currentStreak: Int,
        val longestStreak: Int,
        val totalPushups: Int,
        val totalDeepWorkMinutes: Int,
        val totalResetsCompleted: Int,
        val currentResetDay: Int
    )
}