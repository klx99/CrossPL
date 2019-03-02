import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    defaultConfig {
        applicationId = "org.elastos.tools.crosslang.test"
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["targetSdkVersion"] as Int)
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String
    }

    sourceSets["main"].java.srcDirs("src/main/java", "src/main/kotlin")
    sourceSets["main"].withConvention(KotlinSourceSet::class) {
        kotlin.srcDir("src/main/kotlin")
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
//    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))

    implementation(project(":lib"))
}

apply(plugin = rootProject.extra["groupId"] as String)
