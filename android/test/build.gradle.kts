import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    defaultConfig {
        applicationId = "${rootProject.extra["groupId"]}.test"
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["targetSdkVersion"] as Int)
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        javaCompileOptions.annotationProcessorOptions.includeCompileClasspath = true
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
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
//    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))

    compileOnly(project(":anno"))
    kapt(project(":anno"))
//    annotationProcessor(project(":anno"))

    implementation(project(":lib"))
}

// native build >>>
android {
    defaultConfig {
        ndk {
            abiFilters("armeabi-v7a", "arm64-v8a", "x86_64")
        }

    }
    externalNativeBuild {
        cmake {
            setPath("src/main/cpp/CMakeLists.txt")
        }
    }
}

tasks {
    val cleanNativeBuild by creating (Delete::class) {
        delete(".externalNativeBuild")
    }
    clean {
        dependsOn(cleanNativeBuild)
    }
}
// native build <<<

