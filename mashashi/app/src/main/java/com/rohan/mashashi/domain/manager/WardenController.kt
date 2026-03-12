package com.rohan.mashashi.domain.manager

import com.rohan.mashashi.domain.logic.PushupCounter

class WardenController {
    companion object {
        // Whitelist of allowed apps (educational, communication, system)
        private val WHITELIST = setOf(
            "com.whatsapp",           // Essential communication
            "com.android.phone",      // System dialer
            "com.android.contacts",   // Contacts
            "com.google.android.dialer", // Google dialer
            "com.google.android.apps.messaging" // Messaging
        )

        /**
         * Determines if an app should be locked based on the whitelist
         * @param packageName The package name of the foreground app
         * @return True if the app should be locked, false if allowed
         */
        fun shouldLock(packageName: String): Boolean {
            // Check if package name is in whitelist
            if (packageName in WHITELIST) {
                return false
            }
            
            // Check for system apps (com.android.*)
            if (packageName.startsWith("com.android.")) {
                return false
            }
            
            // Check for Google apps (com.google.*)
            if (packageName.startsWith("com.google.")) {
                return false
            }
            
            // All other apps should be locked
            return true
        }

        /**
         * Checks if accessibility service is enabled
         */
        fun isAccessibilityEnabled(context: android.content.Context): Boolean {
            val accessibilityManager = context.getSystemService(
                android.content.Context.ACCESSIBILITY_SERVICE
            ) as android.accessibilityservice.AccessibilityManager
            
            val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
                android.accessibilityservice.AccessibilityService.SERVICE_MASK
            )
            
            return enabledServices.any { serviceInfo ->
                serviceInfo.id == "com.rohan.mashashi/com.rohan.mashashi.services.WardenAccessibilityService"
            }
        }

        /**
         * Checks if overlay permission is granted
         */
        fun isOverlayPermissionGranted(context: android.content.Context): Boolean {
            return android.provider.Settings.canDrawOverlays(context)
        }

        /**
         * Check if blackout can be dismissed (push-up target reached)
         */
        fun canDismissBlackout(pushupCounter: PushupCounter): Boolean {
            return pushupCounter.isTargetReached()
        }
    }
}