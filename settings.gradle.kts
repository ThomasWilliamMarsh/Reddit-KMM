pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}


rootProject.name = "Reddit_KMM"
enableFeaturePreview("VERSION_CATALOGS")
include(":androidApp")
include(":shared")