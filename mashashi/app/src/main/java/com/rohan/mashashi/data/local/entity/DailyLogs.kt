package com.rohan.mashashi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_logs")
class DailyLogs(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String, // YYYY-MM-DD format
    val pushupsCompleted: Int = 0,
    val deepWorkMinutes: Int = 0,
    val wisdomPoints: Int = 0, // Journal entries, reading, etc.
    val disciplinePoints: Int = 0, // Habit completion, resets
    val streak: Int = 0,
    val completed: Boolean = false // Whether all daily goals were met
)