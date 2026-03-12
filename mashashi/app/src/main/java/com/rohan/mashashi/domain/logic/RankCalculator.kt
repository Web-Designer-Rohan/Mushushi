package com.rohan.mashashi.domain.logic

class RankCalculator {
    companion object {
        // Rank thresholds based on total XP
        private const val RANK_E_MAX = 500
        private const val RANK_D_MAX = 1500
        private const val RANK_C_MAX = 5000
        private const val RANK_B_MAX = 15000
        private const val RANK_A_MAX = 50000

        // Rank names in order
        private val RANKS = listOf("E", "D", "C", "B", "A", "S")

        /**
         * Calculate current rank based on total XP
         */
        fun calculateRank(totalXp: Int): String {
            return when (totalXp) {
                in 0..RANK_E_MAX -> "E"
                in RANK_E_MAX + 1..RANK_D_MAX -> "D"
                in RANK_D_MAX + 1..RANK_C_MAX -> "C"
                in RANK_C_MAX + 1..RANK_B_MAX -> "B"
                in RANK_B_MAX + 1..RANK_A_MAX -> "A"
                else -> "S"
            }
        }

        /**
         * Get rank thresholds for display
         */
        fun getRankThresholds(): List<RankThreshold> {
            return listOf(
                RankThreshold("E", 0, RANK_E_MAX),
                RankThreshold("D", RANK_E_MAX + 1, RANK_D_MAX),
                RankThreshold("C", RANK_D_MAX + 1, RANK_C_MAX),
                RankThreshold("B", RANK_C_MAX + 1, RANK_B_MAX),
                RankThreshold("A", RANK_B_MAX + 1, RANK_A_MAX),
                RankThreshold("S", RANK_A_MAX + 1, Int.MAX_VALUE)
            )
        }

        /**
         * Get rank color based on rank level
         */
        fun getRankColor(rank: String): Int {
            return when (rank) {
                "E" -> 0xFF7F8C8D // Gray
                "D" -> 0xFF95A5A6 // Light Gray
                "C" -> 0xFF3498DB // Blue
                "B" -> 0xFF2ECC71 // Green
                "A" -> 0xFFF39C12 // Orange
                "S" -> 0xFFE74C3C // Red
                else -> 0xFF000000 // Black
            }
        }

        /**
         * Get rank progress percentage
         */
        fun getRankProgress(totalXp: Int): Int {
            return when (totalXp) {
                in 0..RANK_E_MAX -> (totalXp * 100 / RANK_E_MAX)
                in RANK_E_MAX + 1..RANK_D_MAX -> (totalXp - RANK_E_MAX) * 100 / (RANK_D_MAX - RANK_E_MAX)
                in RANK_D_MAX + 1..RANK_C_MAX -> (totalXp - RANK_D_MAX) * 100 / (RANK_C_MAX - RANK_D_MAX)
                in RANK_C_MAX + 1..RANK_B_MAX -> (totalXp - RANK_C_MAX) * 100 / (RANK_B_MAX - RANK_C_MAX)
                in RANK_B_MAX + 1..RANK_A_MAX -> (totalXp - RANK_B_MAX) * 100 / (RANK_A_MAX - RANK_B_MAX)
                else -> (totalXp - RANK_A_MAX) * 100 / (Int.MAX_VALUE - RANK_A_MAX)
            }
        }

        data class RankThreshold(
            val rank: String,
            val minXp: Int,
            val maxXp: Int
        )
    }
}