# TECH_STACK: MASHASHI (The Solo Leveling Engine)

## 1. CORE LANGUAGE & FRAMEWORK
* **Language:** Kotlin (1.9+) - The modern, type-safe standard for Android.
* **UI Framework:** Jetpack Compose - For building the premium "vibe" UI with declarative code.
* **Minimum SDK:** Android 8.0 (API 26) - Required for advanced background services and notification channels.
* **Target SDK:** Android 14 (API 34) - To ensure compatibility with the latest system security and camera features.

## 2. ARCHITECTURE PATTERN
* **Pattern:** MVVM (Model-View-ViewModel) + Clean Architecture.
* **Dependency Injection:** Hilt (Dagger) - To manage the lifecycle of AI services and database instances.
* **Asynchronous Handling:** Kotlin Coroutines & Flow - Essential for smooth UI and real-time rep counting without lag.

## 3. DESIGN & UI ENGINE
* **Theme:** Material 3 (Customized for Dark Mode/Glassmorphism).
* **Animations:** * Compose Animation API for layout transitions.
    * Lottie for high-end "Solo Leveling" rank-up animations.
    * Canvas API for the custom audio/focus waveforms seen in the design mockup.
* **Typography:** Inter or SF Pro (loaded via Google Fonts API).

## 4. INTELLIGENCE & COMPUTER VISION
* **Pose Detection:** Google ML Kit (Pose Detection API) - High-performance, on-device skeletal tracking for workout verification.
* **AI Integration:** Google Gemini API - Powering the "System Architect" chat and daily growth evaluations.
* **Camera Library:** CameraX - For reliable, lifecycle-aware camera access during workouts.

## 5. LOCAL STORAGE & DATA
* **Database:** Room Persistence Library - To store XP, stats, habits, and "If-Then" routines locally.
* **Preferences:** DataStore - For storing light settings like "Blackout Mode" schedules.
* **Cloud Sync (Optional):** Supabase (PostgreSQL + Auth) - For secure backup of your 90-day progress.

## 6. SYSTEM CONTROL (THE "FORCEFUL" TOOLS)
* **Accessibility Service:** To monitor foreground apps and enforce the "Educational Only" filter.
* **Device Admin/Overlay:** To trigger the Blackout Screen over other applications.
* **WorkManager:** To handle background tasks like task reminders and "NSFW Shield" monitoring.

## 7. DEVOPS & COMPILATION
* **CLI Tool:** Kilo CLI (Environment: Termux/Android).
* **CI/CD:** GitHub Actions - Automating the build process to generate the signed `.apk` file for testing.
* **Version Control:** Git.
