// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    apply(from = rootProject.projectDir.absolutePath + "/gradle/config.gradle.kts")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:" + extra["androidBuildVersion"] as String)
        classpath(kotlin("gradle-plugin", version = extra["kotlinVersion"] as String))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = uri(rootProject.buildDir.absolutePath + "/repo")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(from = rootProject.projectDir.absolutePath + "/gradle/config.gradle.kts")
println("versionCode = " + rootProject.extra["versionCode"])
println("versionName = " + rootProject.extra["versionName"])


