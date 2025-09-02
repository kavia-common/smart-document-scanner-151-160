pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.experimental.android-ecosystem").version("0.1.43")
}

rootProject.name = "smart-document-scanner"

include("app")

defaults {
    androidApplication {
        jdkVersion = 17
        compileSdk = 34
        minSdk = 26

        versionCode = 1
        versionName = "0.1.0"
        applicationId = "com.camscanner.app"

        testing {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.10.2")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }
    }
}
