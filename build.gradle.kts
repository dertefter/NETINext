// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.6" apply false
    alias(libs.plugins.kotlin.compose) apply false
}
