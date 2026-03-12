# CONTEXT: MASHASHI DEVELOPMENT STATE

## 1. PROJECT STATUS
* **Current Phase:** Initial Infrastructure & Project Scaffolding.
* **Environment:** Android (Termux) via Kilo Code CLI.
* **Primary Target:** Developing a functional prototype that integrates System Overlays with Pose Detection.

## 2. SYSTEM ENVIRONMENT
* **Device:** Vivo Y28 5G (Android-based).
* **Entry Point:** `MainActivity.kt` utilizing Jetpack Compose.
* **Tooling:** GitHub Actions for remote compilation to APK.
* **Permissions Context:** The app requires high-level privileges:
    * `CAMERA` (for workout verification).
    * `ACCESSIBILITY_SERVICE` (for app blocking).
    * `SYSTEM_ALERT_WINDOW` (for the Blackout Overlay).
    * `POST_NOTIFICATIONS` (for task reminders).

## 3. CORE LOGIC CONTEXT
### The "Solo Leveling" Algorithm
The app must track a global `UserLevel` and `TotalXP`. Every feature (Workout, Focus, Journal) feeds into this central state. 
* **State of the XP Engine:** Currently being defined as a local Room database.
* **State of the Vision Engine:** Planning stage for ML Kit integration.

## 4. IMMEDIATE CHALLENGES
* **Termux Constraints:** Kilo CLI must handle dependencies that are compatible with the local Termux environment and the GitHub Action runner.
* **Battery Optimization:** Because Mashashi monitors foreground apps, the AI must implement `ForegroundServices` with low-power consumption logic to prevent the OS from killing the "Warden" service.
* **Social Whitelisting:** The "Blackout" mode must specifically exclude WhatsApp and the Phone Dialer, requiring a dynamic package-filtering mechanism.

## 5. USER CONTEXT: ROHAN BISWAS
* **Profile:** A "Vibe Coder" with 3 years of web design experience, currently scaling into Android System development.
* **Philosophy:** Practicing Brahmacharya and disciplined growth. The app must reflect this "No-Nonsense" and "Elite" lifestyle.
* **Workflow:** Rohan provides prompts to Kilo CLI -> Kilo CLI generates code -> GitHub Actions compiles the APK -> Rohan tests on the physical device.

## 6. DEVELOPMENT ROADMAP (CURRENT SPRINT)
1. **Sprint 1:** Setup Project Structure, Theme (The Void), and Navigation.
2. **Sprint 2:** Implement the "Warden" (Accessibility Service & Basic App Blocker).
3. **Sprint 3:** Integrate ML Kit for the first Workout Quest (Push-ups).
4. **Sprint 4:** Connect the AI Oracle (Gemini API) for reflections.
