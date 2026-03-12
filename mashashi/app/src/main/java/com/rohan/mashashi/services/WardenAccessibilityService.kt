package com.rohan.mashashi.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class WardenAccessibilityService : AccessibilityService() {
    private val TAG = "WardenService"
    private var currentPackageName: String? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Monitor window state changes to detect foreground apps
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            currentPackageName = event.packageName?.toString()
            
            // Log the detected package name
            currentPackageName?.let { packageName ->
                Log.d(TAG, "Detected foreground app: $packageName")
                
                // Check if we should lock the app
                WardenController.shouldLock(packageName)?.let { shouldLock ->
                    if (shouldLock) {
                        Log.d(TAG, "Locking $packageName - not in whitelist")
                        // TODO: Trigger blackout overlay
                    } else {
                        Log.d(TAG, "Allowing $packageName - in whitelist")
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Warden service connected")
    }

    companion object {
        fun isAccessibilityEnabled(context: Context): Boolean {
            val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as android.accessibilityservice.AccessibilityManager
            val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityService.SERVICE_MASK)
            
            return enabledServices.any { serviceInfo ->
                serviceInfo.id == "com.rohan.mashashi/com.rohan.mashashi.services.WardenAccessibilityService"
            }
        }
    }
}