package com.rohan.mashashi.data.vision

import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions

class PoseDetectorProcessor {
    private val TAG = "PoseDetector"
    private val poseDetector: PoseDetector

    init {
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        
        poseDetector = PoseDetection.getClient(options)
        Log.d(TAG, "PoseDetector initialized with accurate options")
    }

    /**
     * Process a camera frame and detect poses
     */
    suspend fun processFrame(imageProxy: ImageProxy): Pose? {
        val image = InputImage.fromMediaImage(
            imageProxy.image!!,
            imageProxy.imageInfo.rotationDegrees
        )

        return try {
            val pose = poseDetector.process(image)
            imageProxy.close()
            pose
        } catch (e: Exception) {
            Log.e(TAG, "Error processing pose frame: ${e.message}")
            imageProxy.close()
            null
        }
    }

    /**
     * Extract key joint coordinates from pose
     */
    fun extractJointCoordinates(pose: Pose): Map<PoseLandmark.Type, android.graphics.PointF> {
        val joints = mutableMapOf<PoseLandmark.Type, android.graphics.PointF>()
        
        // Key joints for push-up detection
        val keyJoints = listOf(
            PoseLandmark.Type.LEFT_SHOULDER,
            PoseLandmark.Type.RIGHT_SHOULDER,
            PoseLandmark.Type.LEFT_ELBOW,
            PoseLandmark.Type.RIGHT_ELBOW,
            PoseLandmark.Type.LEFT_WRIST,
            PoseLandmark.Type.RIGHT_WRIST,
            PoseLandmark.Type.LEFT_HIP,
            PoseLandmark.Type.RIGHT_HIP
        )

        keyJoints.forEach { jointType ->
            pose.getPoseLandmark(jointType)?.let { landmark ->
                joints[jointType] = android.graphics.PointF(landmark.position.x, landmark.position.y)
            }
        }

        return joints
    }

    /**
     * Calculate elbow angle using Law of Cosines
     */
    fun calculateElbowAngle(
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
     * Detect push-up position (DOWN if angle < 90°, UP if angle > 160°)
     */
    fun detectPushupPosition(angle: Double): PushupPosition {
        return when {
            angle < 90.0 -> PushupPosition.DOWN
            angle > 160.0 -> PushupPosition.UP
            else -> PushupPosition.MID
        }
    }

    /**
     * Detect push-up state machine
     */
    fun detectPushupState(
        leftAngle: Double,
        rightAngle: Double,
        previousState: PushupState
    ): PushupState {
        val leftPosition = detectPushupPosition(leftAngle)
        val rightPosition = detectPushupPosition(rightAngle)

        // Both arms must agree on position
        val currentPosition = when {
            leftPosition == PushupPosition.DOWN &8 rightPosition == PushupPosition.DOWN -> PushupPosition.DOWN
            leftPosition == PushupPosition.UP && rightPosition == PushupPosition.UP -> PushupPosition.UP
            else -> PushupPosition.MID
        }

        // State machine logic
        return when (previousState) {
            PushupState.IDLE -> {
                if (currentPosition == PushupPosition.DOWN) PushupState.DOWN else PushupState.IDLE
            }
            PushupState.DOWN -> {
                if (currentPosition == PushupPosition.UP) PushupState.UP else PushupState.DOWN
            }
            PushupState.UP -> {
                if (currentPosition == PushupPosition.DOWN) PushupState.DOWN else PushupState.UP
            }
            PushupState.COMPLETED -> PushupState.IDLE
        }
    }

    /**
     * Check if rep is valid (both arms in sync)
     */
    fun isRepValid(leftAngle: Double, rightAngle: Double): Boolean {
        val leftPosition = detectPushupPosition(leftAngle)
        val rightPosition = detectPushupPosition(rightAngle)

        // Both arms should be in the same position
        return leftPosition == rightPosition
    }

    /**
     * Release resources
     */
    fun release() {
        poseDetector.close()
        Log.d(TAG, "PoseDetector released")
    }

    enum class PushupPosition {
        UP, DOWN, MID
    }

    data class PushupState(
        val state: State,
        val count: Int = 0
    ) {
        enum class State {
            IDLE, DOWN, UP, COMPLETED
        }
    }
}