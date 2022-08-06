plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "marsh.thomas.redditkmm.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(libs.koin.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.android.core)
    implementation(libs.android.lifecycle)
    implementation(libs.android.appcompat)
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.activity)
    implementation(libs.android.compose.foundation)
    implementation(libs.android.compose.material)
    implementation(libs.android.compose.tooling)
    implementation(libs.coil.compose)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.koin.test)
}