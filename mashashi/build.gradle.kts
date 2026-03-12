// Top-level build file where you can add configuration options common to all sub-projects/modules.
gradle.ext.minSdkVersion = 26
gradle.ext.targetSdkVersion = 34
gradle.ext.compileSdkVersion = 34

gitProperties = [
    'version': '1.0.0',
    'commit': 'unknown'
]

// Add build scan plugin if needed
// plugins {
//     id 'com.gradle.enterprise' version '3.14.1'
// }

// Build scan configuration
// gradleEnterprise {
//     buildScan {
//         termsOfServiceUrl = 'https://gradle.com/terms-of-service'
//         termsOfServiceAgree = 'yes'
//     }
// }