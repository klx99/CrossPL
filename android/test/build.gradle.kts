import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "org.elastos.tools.build.test"
        minSdkVersion(16)
        targetSdkVersion(27)
        versionCode = 1
        versionName = "1.0"
    }

    sourceSets["main"].withConvention(KotlinSourceSet::class) {
        kotlin.srcDir(file("src/main/kotlin"))
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))
}

apply(plugin = "org.elastos.tools.build")
