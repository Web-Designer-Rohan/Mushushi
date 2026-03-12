package com.rohan.mashashi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_stats")
class UserStats(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val level: Int = 1,
    val totalXp: Int = 0,
    val strengthXp: Int = 0,
    val focusXp: Int = 0,
    val wisdomXp: Int = 0,
    val disciplineXp: Int = 0,
    val currentRank: String = "E",
    val lastRankUpTimestamp: Long = 0L,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalPushups: Int = 0,
    val totalDeepWorkMinutes: Int = 0,
    val totalResetsCompleted: Int = 0,
    val currentResetDay: Int = 0
)