import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"] as Int)
    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"] as Int)
        targetSdkVersion(rootProject.extra["targetSdkVersion"] as Int)
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        javaCompileOptions.annotationProcessorOptions.includeCompileClasspath = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
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

// native build >>>
android {
    defaultConfig {
        ndk {
            abiFilters("armeabi-v7a", "arm64-v8a", "x86_64")
        }

    }
    externalNativeBuild {
        cmake {
            setPath("CMakeLists.txt")
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

tasks.preBuild.dependsOn(":anno:publish")
dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))

    compileOnly("${rootProject.extra["groupId"]}:anno:+")
    kapt("${rootProject.extra["groupId"]}:anno:+")
//    annotationProcessor(project(":anno"))
}

extra["publishDependsOn"] = "assembleRelease"
extra["publishArtifact"] = "${project.buildDir}/outputs/aar/lib.aar"
extra["publishGroupId"] = rootProject.extra["groupId"]
extra["publishArtifactId"] = "lib"
extra["publishVersion"] = rootProject.extra["versionName"]
apply(from = rootProject.projectDir.absolutePath + "/gradle/publish.gradle.kts")
