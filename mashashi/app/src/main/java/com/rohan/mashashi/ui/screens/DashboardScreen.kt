package com.rohan.mashashi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohan.mashashi.ui.viewmodels.MainViewModel
import com.rohan.mashashi.ui.viewmodels.MainViewModel
import com.rohan.mashashi.ui.theme.MashashiTheme
import kotlin.math.cos
import kotlin.math.sin

class DashboardScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MashashiTheme {
                DashboardScreenContent()
            }
        }
    }
}

@Composable
fun DashboardScreenContent() {
    val viewModel: MainViewModel = viewModel()
    
    // Collect all necessary flows
    val rankInfo by viewModel.rankInfo.collectAsState()
    val streakInfo by viewModel.streakInfo.collectAsState()
    val userStats by viewModel.userStats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Rank Display
        RankDisplay(rank = rankInfo.rank, progress = rankInfo.progress)

        Spacer(modifier = Modifier.height(32.dp))

        // XP Progress Bars
        XPProgressBars(
            strengthXp = rankInfo.strengthXp,
            focusXp = rankInfo.focusXp,
            wisdomXp = rankInfo.wisdomXp,
            disciplineXp = rankInfo.disciplineXp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Streak Counter
        StreakCounter(streakInfo = streakInfo)

        Spacer(modifier = Modifier.height(32.dp))

        // Stats Grid
        StatsGrid(userStats = userStats)
    }
}

@Composable
fun RankDisplay(rank: String, progress: Int) {
    val rankColor = when (rank) {
        "E" -> Color(0xFF7F8C8D)
        "D" -> Color(0xFF95A5A6)
        "C" -> Color(0xFF3498DB)
        "B" -> Color(0xFF2ECC71)
        "A" -> Color(0xFFF39C12)
        "S" -> Color(0xFFE74C3C)
        else -> Color.Black
    }

    // Create glow effect using Canvas
    val glowColor = androidx.compose.ui.graphics.Color(r = rankColor.red, g = rankColor.green, b = rankColor.blue, a = 200)
    val innerColor = androidx.compose.ui.graphics.Color(r = rankColor.red, g = rankColor.green, b = rankColor.blue, a = 255)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Glow effect using Canvas
        androidx.compose.ui.graphics.Canvas(
            modifier = Modifier
                .size(180.dp, 180.dp)
                .padding(8.dp),
            onDraw = {
                // Draw multiple glow circles
                for (i in 10 downTo 1) {
                    val alpha = (i * 20).toFloat()
                    val radius = (180f - i * 4f) / 2f
                    drawCircle(
                        color = androidx.compose.ui.graphics.Color(r = rankColor.red, g = rankColor.green, b = rankColor.blue, a = alpha),
                        radius = radius,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
            }
        )

        // Main rank letter with glow
        Text(
            text = rank,
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = innerColor,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Progress text
        Text(
            text = "$progress% to next rank",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun XPProgressBars(
    strengthXp: Int,
    focusXp: Int,
    wisdomXp: Int,
    disciplineXp: Int
) {
    val totalXp = strengthXp + focusXp + wisdomXp + disciplineXp
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        XPBar(
            label = "Strength",
            xp = strengthXp,
            color = androidx.compose.ui.graphics.Color(0xFF3498DB),
            percentage = if (totalXp > 0) (strengthXp * 100 / totalXp) else 0
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        XPBar(
            label = "Focus",
            xp = focusXp,
            color = androidx.compose.ui.graphics.Color(0xFF2ECC71),
            percentage = if (totalXp > 0) (focusXp * 100 / totalXp) else 0
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        XPBar(
            label = "Wisdom",
            xp = wisdomXp,
            color = androidx.compose.ui.graphics.Color(0xFFF39C12),
            percentage = if (totalXp > 0) (wisdomXp * 100 / totalXp) else 0
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        XPBar(
            label = "Discipline",
            xp = disciplineXp,
            color = androidx.compose.ui.graphics.Color(0xFFE74C3C),
            percentage = if (totalXp > 0) (disciplineXp * 100 / totalXp) else 0
        )
    }
}

@Composable
fun XPBar(label: String, xp: Int, color: androidx.compose.ui.graphics.Color, percentage: Int) {
    val barHeight = 12.dp
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "$xp XP",
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Progress bar with rounded corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(4.dp))
                .background(androidx.compose.ui.graphics.Color(0xFF2C3E50))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage / 100f)
                    .height(barHeight)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

@Composable
fun StreakCounter(streakInfo: MainViewModel.StreakInfo) {
    val currentStreak = streakInfo.currentStreak
    val longestStreak = streakInfo.longestStreak
    val currentResetDay = streakInfo.currentResetDay

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Current streak
        Text(
            text = "Current Streak",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "$currentStreak days",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 90-Day Reset Progress
        Text(
            text = "90-Day Reset Progress",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "$currentResetDay/90 days",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Longest streak
        Text(
            text = "Longest Streak",
            fontSize = 14.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "$longestStreak days",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}

@Composable
fun StatsGrid(userStats: MainViewModel.UserStats) {
    val totalPushups = userStats.totalPushups
    val totalDeepWorkMinutes = userStats.totalDeepWorkMinutes
    val totalResetsCompleted = userStats.totalResetsCompleted

    val stats = listOf(
        Triple("Push-ups", "$totalPushups", androidx.compose.ui.graphics.Color(0xFF3498DB)),
        Triple("Deep Work", "${totalDeepWorkMinutes}m", androidx.compose.ui.graphics.Color(0xFF2ECC71)),
        Triple("Resets", "$totalResetsCompleted", androidx.compose.ui.graphics.Color(0xFFF39C12))
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Lifetime Stats",
            fontSize = 16.sp,
            color = androidx.compose.ui.graphics.Color.Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            stats.forEach { (label, value, color) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        }
    }
}