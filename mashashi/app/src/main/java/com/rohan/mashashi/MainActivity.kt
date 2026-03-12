package com.rohan.mashashi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohan.mashashi.ui.theme.MashashiTheme
import com.rohan.mashashi.ui.components.BlackoutOverlay

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    
    // Request codes for permissions
    private val requestAccessibilityPermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Accessibility permission result: ${result.resultCode}")
        checkPermissions()
    }
    
    private val requestOverlayPermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Overlay permission result: ${result.resultCode}")
        checkPermissions()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MashashiTheme {
                // Setup screen
                SetupScreen()
            }
        }
    }

    @Composable
    fun SetupScreen() {
        val context = LocalContext.current
        val isAccessibilityEnabled = WardenController.isAccessibilityEnabled(context)
        val isOverlayPermissionGranted = WardenController.isOverlayPermissionGranted(context)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mashashi Setup",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            // Accessibility Service Permission
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Accessibility Service",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isAccessibilityEnabled) FontWeight.Bold else FontWeight.Normal
                )
                
                if (isAccessibilityEnabled) {
                    Text(
                        text = "✓",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Button(
                        onClick = {
                            openAccessibilitySettings(context)
                        }
                    ) {
                        Text("Enable")
                    }
                }
            }
            
            // Overlay Permission
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Draw Over Apps",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isOverlayPermissionGranted) FontWeight.Bold else FontWeight.Normal
                )
                
                if (isOverlayPermissionGranted) {
                    Text(
                        text = "✓",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Button(
                        onClick = {
                            requestOverlayPermission()
                        }
                    ) {
                        Text("Enable")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Continue Button
            if (isAccessibilityEnabled && isOverlayPermissionGranted) {
                Button(
                    onClick = {
                        // TODO: Navigate to main app
                        Log.d(TAG, "Starting Mashashi...")
                    }
                ) {
                    Text("Start Mashashi")
                }
            } else {
                Button(
                    onClick = { checkPermissions() },
                    enabled = false
                ) {
                    Text("Start Mashashi")
                }
            }
        }
    }

    private fun checkPermissions() {
        val context = this
        val isAccessibilityEnabled = WardenController.isAccessibilityEnabled(context)
        val isOverlayPermissionGranted = WardenController.isOverlayPermissionGranted(context)
        
        Log.d(TAG, "Accessibility: $isAccessibilityEnabled, Overlay: $isOverlayPermissionGranted")
    }

    private fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        requestAccessibilityPermission.launch(intent)
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}"))
            requestOverlayPermission.launch(intent)
        }
    }
}