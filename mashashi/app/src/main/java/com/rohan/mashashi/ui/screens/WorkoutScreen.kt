package com.rohan.mashashi.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.pose.Pose
import com.rohan.mashashi.data.vision.PoseDetectorProcessor
import com.rohan.mashashi.data.vision.PoseDetectorProcessor.PushupState
import com.rohan.mashashi.domain.logic.PushupCounter
import com.rohan.mashashi.ui.theme.MashashiTheme
import com.rohan.mashashi.ui.components.BlackoutOverlay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WorkoutScreen : ComponentActivity() {
    private val TAG = "WorkoutScreen"
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private var poseDetectorProcessor: PoseDetectorProcessor? = null
    private val pushupCounter = PushupCounter()

    // Request camera permission
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            setupCamera()
        } else {
            Log.e(TAG, "Camera permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MashashiTheme {
                WorkoutScreenContent()
            }
        }
    }

    @Composable
    fun WorkoutScreenContent() {
        val context = LocalContext.current
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isCameraPermissionGranted) {
            // Request camera permission
            requestCameraPermission.launch(Manifest.permission.CAMERA)
            SetupCameraPermissionScreen()
        } else {
            // Camera permission granted, start workout
            StartWorkoutScreen()
        }
    }

    @Composable
    fun SetupCameraPermissionScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mashashi Workout",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Camera permission required",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Button(
                onClick = {
                    requestCameraPermission.launch(Manifest.permission.CAMERA)
                }
            ) {
                Text("Grant Camera Permission")
            }
        }
    }

    @Composable
    fun StartWorkoutScreen() {
        val targetReps = 20 // Default target
        val pushupCounter = remember { PushupCounter() }
        pushupCounter.setTarget(targetReps)

        val (isProcessing, setIsProcessing) = remember { mutableStateOf(false) }
        val (repCount, setRepCount) = remember { mutableStateOf(0) }
        val (isValid, setIsValid) = remember { mutableStateOf(false) }
        val (currentAngle, setCurrentAngle) = remember { mutableStateOf(0.0) }
        val (currentPosition, setCurrentPosition) = remember { mutableStateOf("IDLE") }
        val (completedRep, setCompletedRep) = remember { mutableStateOf(false) }

        LaunchedEffect(key1 = true) {
            // Initialize camera and pose detection
            setupCamera()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Camera Preview
            PreviewView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.Black),
                cameraProvider = cameraProvider
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Workout Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Target Reps
                Text(
                    text = "Target: $targetReps reps",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Current Reps
                Text(
                    text = "Reps: $repCount",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Posture Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PostureIndicator(
                        isValid = isValid,
                        angle = currentAngle,
                        position = currentPosition
                    )
                }

                // Start/Stop Button
                Button(
                    onClick = {
                        if (isProcessing) {
                            // Stop processing
                            setIsProcessing(false)
                        } else {
                            // Start processing
                            setIsProcessing(true)
                            pushupCounter.reset()
                            setRepCount(0)
                        }
                    }
                ) {
                    Text(if (isProcessing) "Stop" else "Start")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Message
                Text(
                    text = if (completedRep) "Great! Keep going!" else "Maintain form",
                    fontSize = 14.sp,
                    color = if (completedRep) MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        // Blackout overlay if target not reached
        if (!pushupCounter.isTargetReached()) {
            BlackoutOverlay(
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    fun PostureIndicator(
        isValid: Boolean,
        angle: Double,
        position: String
    ) {
        val color = if (isValid) {
            MaterialTheme.colorScheme.success
        } else {
            MaterialTheme.colorScheme.error
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Angle Display
            Text(
                text = "Angle: ${String.format("%.1f", angle)}°",
                fontSize = 14.sp,
                color = color,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Position Display
            Text(
                text = "Position: $position",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Visual Indicator
            Spacer(modifier = Modifier.height(8.dp))
            
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background circle
                Box(modifier = Modifier.size(80.dp).background(color)) {
                    // Checkmark or X
                    if (isValid) {
                        Text(
                            text = "✓",
                            fontSize = 32.sp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "✗",
                            fontSize = 32.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    private fun setupCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(findViewById<PreviewView>(R.id.preview_view).surfaceProvider)
            }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
                    processImageProxy(imageProxy)
                })
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error binding camera use cases", e)
        }
    }

    private suspend fun processImageProxy(imageProxy: ImageProxy) {
        if (poseDetectorProcessor == null) {
            poseDetectorProcessor = PoseDetectorProcessor()
        }

        val pose = poseDetectorProcessor?.processFrame(imageProxy)
        
        if (pose != null) {
            val joints = poseDetectorProcessor?.extractJointCoordinates(pose)
            
            if (joints != null) {
                val leftShoulder = joints[PoseDetectorProcessor.PoseLandmark.Type.LEFT_SHOULDER]
                val leftElbow = joints[PoseDetectorProcessor.PoseLandmark.Type.LEFT_ELBOW]
                val leftWrist = joints[PoseDetectorProcessor.PoseLandmark.Type.LEFT_WRIST]
                val rightShoulder = joints[PoseDetectorProcessor.PoseLandmark.Type.RIGHT_SHOULDER]
                val rightElbow = joints[PoseDetectorProcessor.PoseLandmark.Type.RIGHT_ELBOW]
                val rightWrist = joints[PoseDetectorProcessor.PoseLandmark.Type.RIGHT_WRIST]

                val result = pushupCounter.processPose(
                    leftShoulder, leftElbow, leftWrist,
                    rightShoulder, rightElbow, rightWrist
                )

                // Update UI (this would need to be posted to main thread)
                Log.d(TAG, "Rep: ${result.count}, State: ${result.state}, Valid: ${result.isValid}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        poseDetectorProcessor?.release()
    }
}