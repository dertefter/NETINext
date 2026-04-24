import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

android {
    namespace = "com.dertefter.neticlient"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.dertefter.neticlient"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:design"))
    implementation(project(":core:navigation"))

    implementation(project(":feature:auth"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:contact_info"))
    implementation(project(":feature:home"))
    implementation(project(":feature:news_detail"))
    implementation(project(":feature:search_group"))
    implementation(project(":feature:calendar"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:settings_account"))
    implementation(project(":feature:settings_labs"))
    implementation(project(":feature:settings_theme"))
    implementation(project(":feature:settings_notifications"))

    implementation(project(":feature:messages"))
    implementation(project(":feature:messages_detail"))
    implementation(project(":feature:search_person"))
    implementation(project(":feature:person_detail"))
    implementation(project(":feature:lesson_detail"))
    implementation(project(":feature:sessia_results"))
    implementation(project(":feature:share_score"))
    implementation(project(":feature:image_viewer"))

    implementation(libs.core.ktx)
    implementation(libs.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.compose.adaptive)
    implementation(libs.androidx.compose.adaptive.layout)
    implementation(libs.androidx.compose.adaptive.navigation)


    implementation(libs.kotlinx.serialization.json)

    implementation(libs.haze)
    implementation(libs.haze.materials)
    implementation(libs.coil)
}
