# ARCHITECTURE: MASHASHI SYSTEM

## 1. ARCHITECTURAL PATTERN
Mashashi utilizes **Clean Architecture** combined with **MVVM (Model-View-ViewModel)**. This separates the business logic (the "Solo Leveling" rules) from the UI (the "Vibe"), ensuring the app is modular, testable, and scalable.

## 2. THE LAYERS

### A. Presentation Layer (UI & ViewModels)
* **Jetpack Compose:** Handles the "Vibe" UI, animations, and glassmorphism effects.
* **ViewModels:** Holds the state of the screen (e.g., current XP, active quest). It communicates with the Use Cases.
* **State Management:** Uses Kotlin `StateFlow` to provide real-time updates to the UI.

### B. Domain Layer (The "Brain")
* **Entities:** Core data models like `UserStats`, `Quest`, `Habit`, and `Rank`.
* **Use Cases (Interactors):** Specific business logic actions.
    * `ProcessWorkoutRep.kt`: Logic to verify if a movement counts as a rep.
    * `CalculateRankUpgrade.kt`: Logic for moving from Rank D to Rank C.
    * `EnforceLockdown.kt`: Decision engine for blacking out the screen.
* **Repository Interfaces:** Defines how data should be handled without knowing where it comes from.

### C. Data Layer (The "Body")
* **Repositories Implementation:** Orchestrates data between the local database and the AI API.
* **Room Database:** Local storage for user progress, ensuring the app works offline.
* **Mappers:** Converts raw database objects into Domain Entities.

## 3. SPECIALIZED MODULES

### I. The Vision Engine (Pose Detection)
* **Camera Manager:** Interfaces with CameraX to stream frames.
* **ML Processor:** Passes frames to Google ML Kit.
* **Skeletal Analyzer:** A custom logic layer that calculates joint angles (e.g., "Is the elbow at 90 degrees?") to validate reps.

### II. The Warden (System Control)
* **Accessibility Service:** Monitors the `WindowContentChanged` event to detect which app is in the foreground.
* **Overlay Controller:** Manages the "Blackout" UI layer that sits on top of restricted apps.
* **Policy Engine:** Checks the current "If-Then" rules to see if the user has earned their screen time.

### III. The AI Oracle (Intelligence)
* **Gemini Service:** A dedicated module for sending journal entries for sentiment analysis and receiving motivational feedback.

## 4. DATA FLOW (Reactive Cycle)
1.  **User Action:** User starts a Push-up Quest.
2.  **Vision Engine:** Detects a valid rep and updates the `RepCount` in the Data Layer.
3.  **Use Case:** The `ProcessWorkoutRep` use case checks if the goal is met.
4.  **Database:** Updates the XP and Rank in the Room DB.
5.  **ViewModel:** Detects the DB change and emits a new state.
6.  **UI:** Plays the "Level Up" Lottie animation and unlocks the screen.

## 5. REPOSITORY STRUCTURE
```text
app/
├── data/
│   ├── local/ (Room DB, DAOs)
│   ├── remote/ (Gemini API, Supabase)
│   └── repository/ (Impl)
├── domain/
│   ├── model/ (Entities)
│   ├── repository/ (Interfaces)
│   └── usecase/ (Business Logic)
└── ui/
    ├── components/ (Reusable Glassmorphic Views)
    ├── navigation/ (Screen Routing)
    ├── screens/ (Home, Workout, Journal, Profile)
    └── theme/ (Color.kt, Type.kt)
