plugins {
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
}

val gradleDir = "${rootProject.projectDir.absolutePath.replace("/buildSrc", "")}/gradle/"

dependencies {
    apply(from = "${gradleDir}/config.gradle.kts")
    implementation("com.android.tools.build:gradle:" + rootProject.extra["androidBuildVersion"] as String)

    implementation("com.github.javaparser:javaparser-core:3.12.0")
//    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.12.0")
}

if (rootProject.extra.has("groupId")) {
    extra["publishDependsOn"] = "assemble"
    extra["publishArtifact"] = "${project.buildDir}/libs/buildSrc.jar"
    extra["publishGroupId"] = rootProject.extra["groupId"]
    extra["publishArtifactId"] = "crosspl-gradleplugin"
    extra["publishVersion"] = rootProject.extra["versionName"]
    apply(from = "${gradleDir}/publish.gradle.kts")
}
