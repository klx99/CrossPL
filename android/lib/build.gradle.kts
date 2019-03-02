import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["targetSdkVersion"] as Int)
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    sourceSets["main"].withConvention(KotlinSourceSet::class) {
        kotlin.srcDir(file("src/main/kotlin"))
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))
}

extra["publishDependsOn"] = "assembleRelease"
extra["publishArtifact"] = "${project.buildDir}/outputs/aar/lib.aar"
extra["publishGroupId"] = rootProject.extra["groupId"]
extra["publishArtifactId"] = "cross-lang-generator"
extra["publishVersion"] = rootProject.extra["versionName"]
apply(from = rootProject.projectDir.absolutePath + "/gradle/publish.gradle.kts")
