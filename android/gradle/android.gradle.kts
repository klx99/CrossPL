import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
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

    sourceSets["main"].java.srcDirs("src/main/java", "src/main/kotlin")
    sourceSets["main"].withConvention(KotlinSourceSet::class) {
        kotlin.srcDir("src/main/kotlin")
    }

    compileOptions {
        sourceCompatibility = rootProject.extra["javaVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["javaVersion"] as JavaVersion
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))
}