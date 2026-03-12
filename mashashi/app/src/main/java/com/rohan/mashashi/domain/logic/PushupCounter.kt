package com.rohan.mashashi.domain.logic

import com.rohan.mashashi.data.vision.PoseDetectorProcessor
import com.rohan.mashashi.data.vision.PoseDetectorProcessor.PushupState

class PushupCounter {
    private val TAG = "PushupCounter"
    private var currentState: PushupState.State = PushupState.State.IDLE
    private var repCount = 0
    private var targetCount = 0
    private var lastStateChangeTime = 0L
    private var lastValidAngle = 0.0

    /**
     * Process pose data and update push-up count
     */
    fun processPose(
        leftShoulder: android.graphics.PointF?,
        leftElbow: android.graphics.PointF?,
        leftWrist: android.graphics.PointF?,
        rightShoulder: android.graphics.PointF?,
        rightElbow: android.graphics.PointF?,
        rightWrist: android.graphics.PointF?
    ): PushupResult {
        // Check if all required joints are detected
        if (leftShoulder == null || leftElbow == null || leftWrist == null ||
            rightShoulder == null || rightElbow == null || rightWrist == null) {
            return PushupResult(
                state = currentState,
                count = repCount,
                isValid = false,
                angle = 0.0,
                position = "NO_DETECTION"
            )
        }

        // Calculate elbow angles using Law of Cosines
        val leftAngle = calculateElbowAngle(leftShoulder, leftElbow, leftWrist)
        val rightAngle = calculateElbowAngle(rightShoulder, rightElbow, rightWrist)

        // Check if angles are valid (within reasonable range)
        val isValid = isAngleValid(leftAngle) && isAngleValid(rightAngle)
        
        // Detect push-up position
        val leftPosition = detectPushupPosition(leftAngle)
        val rightPosition = detectPushupPosition(rightAngle)
        val currentPosition = when {
            leftPosition == PoseDetectorProcessor.PushupPosition.DOWN && 
            rightPosition == PoseDetectorProcessor.PushupPosition.DOWN -> "DOWN"
            leftPosition == PoseDetectorProcessor.PushupPosition.UP && 
            rightPosition == PoseDetectorProcessor.PushupPosition.UP -> "UP"
            else -> "MID"
        }

        // State machine logic
        val newState = when (currentState) {
            PushupState.State.IDLE -> {
                if (leftPosition == PoseDetectorProcessor.PushupPosition.DOWN && 
                    rightPosition == PoseDetectorProcessor.PushupPosition.DOWN) {
                    PushupState.State.DOWN
                } else {
                    PushupState.State.IDLE
                }
            }
            PushupState.State.DOWN -> {
                if (leftPosition == PoseDetectorProcessor.PushupPosition.UP && 
                    rightPosition == PoseDetectorProcessor.PushupPosition.UP) {
                    PushupState.State.UP
                } else {
                    PushupState.State.DOWN
                }
            }
            PushupState.State.UP -> {
                if (leftPosition == PoseDetectorProcessor.PushupPosition.DOWN && 
                    rightPosition == PoseDetectorProcessor.PushupPosition.DOWN) {
                    PushupState.State.DOWN
                } else {
                    PushupState.State.UP
                }
            }
        }

        // Check for completed rep (UP -> DOWN -> UP cycle)
        var completedRep = false
        if (currentState == PushupState.State.UP && newState == PushupState.State.DOWN) {
            // Full cycle completed
            completedRep = true
            repCount++
            Log.d(TAG, "Push-up completed! Total: $repCount")
        }

        // Update state
        currentState = newState

        return PushupResult(
            state = currentState,
            count = repCount,
            isValid = isValid,
            angle = (leftAngle + rightAngle) / 2,
            position = currentPosition,
            completedRep = completedRep
        )
    }

    /**
     * Calculate elbow angle using Law of Cosines
     */
    private fun calculateElbowAngle(
        shoulder: android.graphics.PointF,
        elbow: android.graphics.PointF,
        wrist: android.graphics.PointF
    ): Double {
        // Get coordinates
        val x1 = shoulder.x.toDouble()
        val y1 = shoulder.y.toDouble()
        val x2 = elbow.x.toDouble()
        val y2 = elbow.y.toDouble()
        val x3 = wrist.x.toDouble()
        val y3 = wrist.y.toDouble()

        // Calculate distances between points
        val a = Math.sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2)) // shoulder to elbow
        val b = Math.sqrt((x3 - x2).pow(2) + (y3 - y2).pow(2)) // elbow to wrist
        val c = Math.sqrt((x3 - x1).pow(2) + (y3 - y1).pow(2)) // shoulder to wrist

        // Calculate angle using Law of Cosines: c^2 = a^2 + b^2 - 2ab · cos(C)
        val cosC = (a.pow(2) + b.pow(2) - c.pow(2)) / (2 * a * b)
        val angle = Math.toDegrees(Math.acos(cosC))

        return angle
    }

    /**
     * Detect push-up position based on angle
     */
    private fun detectPushupPosition(angle: Double): PoseDetectorProcessor.PushupPosition {
        return when {
            angle < 90.0 -> PoseDetectorProcessor.PushupPosition.DOWN
            angle > 160.0 -> PoseDetectorProcessor.PushupPosition.UP
            else -> PoseDetectorProcessor.PushupPosition.MID
        }
    }

    /**
     * Check if angle is within valid range
     */
    private fun isAngleValid(angle: Double): Boolean {
        return angle > 30.0 && angle < 180.0
    }

    /**
     * Reset counter
     */
    fun reset() {
        repCount = 0
        currentState = PushupState.State.IDLE
        Log.d(TAG, "Push-up counter reset")
    }

    /**
     * Set target count
     */
    fun setTarget(target: Int) {
        targetCount = target
        Log.d(TAG, "Target set to $target reps")
    }

    /**
     * Check if target is reached
     */
    fun isTargetReached(): Boolean {
        return repCount >= targetCount
    }

    data class PushupResult(
        val state: PushupState.State,
        val count: Int,
        val isValid: Boolean,
        val angle: Double,
        val position: String,
        val completedRep: Boolean = false
    )
}