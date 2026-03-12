plugins {
    id 'com.android.application' version '4.2.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.10' apply false
    id 'com.google.devtools.ksp' version '1.9.10-1.0.13' apply false
    id 'com.google.dagger.hilt.android' version '2.5.1' apply false
    id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin' version '1.4.0' apply false
    id 'com.google.gms.google-services' version (9.8.5' apply false
    id 'com.google.firebase.crashlytics' version '9.5.5' apply false
    id 'com.google.firebase.appdistribution' version '9.1.4' apply false
}

// Apply core plugin configuration
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url 'https://plugins.gradle.org/m2/' }
        maven { url 'https://developer.android.com/studio/' }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// Define project-specific properties
gradle.ext {
    minSdkVersion = 26
    targetSdkVersion = 34
    compileSdkVersion = 34
    buildToolsVersion = '3.9.0'
    applicationId = 'com.rohan.mashashi'
    versionCode = 1
    versionName = '1.0.0'
    testInstrumentationRunner = 'androidx.test.runner.AndroidJUnitRunner'
}

// Include all modules in the project
include(':app')