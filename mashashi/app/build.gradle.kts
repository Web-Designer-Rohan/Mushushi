plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.devtools.ksp'
    id 'com.google.dagger.hilt.android'
    id 'com.google.gms.google-services'
    id 'com.google.firebase.crashlytics'
    id 'com.google.firebase.appdistribution'
}

android {
    namespace = 'com.rohan.mashashi'
    
    compileSdk = 34
    buildToolsVersion = '3.9.0'
    
    defaultConfig {
        applicationId = 'com.rohan.mashashi'
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = '1.0.0'
        
        testInstrumentationRunner = 'androidx.test.runner.AndroidJUnitRunner'
        
        // Enable View Binding
        buildFeatures {
            viewBinding = true
            compose = true
        }
        
        // Enable Kotlin coroutines
        kotlinOptions {
            jvmTarget = '17'
        }
        
        // Enable Java 8 features
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        
        // Enable Compose
        composeOptions {
            kotlinCompilerExtensionVersion = '1.5.4'
        }
        
        // Vector drawable support
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    // Build types
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            minifyEnabled false
            debuggable true
        }
    }
    
    // Source sets
    sourceSets {
        main {
            manifest.srcFile 'src/main/AndroidManifest.xml'
            java.srcDirs = ['src/main/java']
            res.srcDirs = ['src/main/res']
            assets.srcDirs = ['src/main/assets']
        }
        test {
            java.srcDirs = ['src/test/java']
        }
        androidTest {
            java.srcDirs = ['src/androidTest/java']
        }
    }
    
    // Compile options
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    // Kotlin options
    kotlinOptions {
        jvmTarget = '17'
    }
    
    // Compose options
    composeOptions {
        kotlinCompilerExtensionVersion = '1.5.4'
    }
    
    // Packaging options
    packaging {
        resources {
            excludes += '/META-INF/{AL2.0,LGPL2.1}'
        }
    }
    
    // AGP 8.1+ requires this for test modules
    testOptions {
        unitTests {
            includeAndroidResources = true
        }
    }
}

dependencies {
    // Core AndroidX
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Compose BOM
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.5'
    
    // Hilt DI
    implementation 'com.google.dagger:hilt-android:2.5.1'
    ksp 'com.google.dagger:hilt-compiler:2.5.1'
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    
    // CameraX
    implementation 'androidx.camera:camera-camera2:1.3.1'
    implementation 'androidx.camera:camera-lifecycle:1.3.1'
    implementation 'androidx.camera:camera-view:1.3.1'
    implementation 'androidx.camera:camera-extensions:1.3.1'
    
    // ML Kit Pose Detection
    implementation 'com.google.mlkit:pose-detection:17.1.0'
    implementation 'com.google.mlkit:pose-detection-accurate:17.1.0'
    
    // Room Database
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    ksp 'androidx.room:room-compiler:2.6.1'
    
    // WorkManager
    implementation 'androidx.work:work-runtime-ktx:2.9.1'
    
    // Firebase
    implementation 'com.google.firebase:firebase-bom:32.7.0'
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation 'com.google.firebase:firebase-crashlytics-ktx'
    
    // Material Design Components
    implementation 'com.google.android.material:material:1.11.0'
    
    // Lottie for animations
    implementation 'com.airbnb.android:lottie-compose:7.1.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    debugImplementation 'androidx.compose.ui:ui-tooling'
    debugImplementation 'androidx.compose.ui:ui-test-manifest'
    
    // MockK for testing
    testImplementation 'io.mockk:mockk:1.13.5'
    
    // Kotlin coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3'
    
    // DataStore
    implementation 'androidx.datastore:datastore-preferences:1.0.0'
    
    // Security
    implementation 'androidx.security:security-crypto:1.1.0-alpha03'
}