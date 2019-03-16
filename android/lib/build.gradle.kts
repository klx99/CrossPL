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

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))
}

extra["publishDependsOn"] = "assembleRelease"
extra["publishArtifact"] = "${project.buildDir}/outputs/aar/lib.aar"
extra["publishGroupId"] = rootProject.extra["groupId"]
extra["publishArtifactId"] = "crosspl"
extra["publishVersion"] = rootProject.extra["versionName"]
apply(from = rootProject.projectDir.absolutePath + "/gradle/publish.gradle.kts")

extra["crosspl"] = mapOf(
     "sourceList" to listOf(
        "NativeBase.kt"
     ),
    "withOnLoadFunc" to true
)
apply(plugin = "${rootProject.extra["groupId"]}.gradleplugin")
