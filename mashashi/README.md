# MASHASHI - The Solo Leveling System

## Project Structure

```
mashashi/
├── app/
│   ├── build.gradle.kts              # App-level Gradle configuration
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml       # App manifest with permissions
│   │   │   ├── java/com/rohan/mashashi/
│   │   │   │   ├── MashashiApplication.kt     # Hilt application class
│   │   │   │   ├── MainActivity.kt            # Main entry point
│   │   │   │   └── ui/
│   │   │   │       └── theme/
│   │   │   │           ├── Theme.kt                # Material 3 theme setup
│   │   │   │           └── Typography.kt           # Typography definitions
│   │   │   └── res/
│   │       ├── values/
│   │       │   ├── colors.xml                # Theme colors (True Black #000000)
│   │       │   └── strings.xml                # String resources
│   │       └── layout/                     # Layout XML files
└── build.gradle.kts                  # Project-level Gradle configuration
settings.gradle.kts                   # Settings configuration