import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

apply(from = rootProject.projectDir.absolutePath + "/gradle/config.gradle.kts")
plugins {
    `kotlin-dsl`
    kotlin("kapt")
}

sourceSets["main"].java.srcDirs("src/main/java", "src/main/kotlin")
sourceSets["main"].withConvention(KotlinSourceSet::class) {
    kotlin.srcDir("src/main/kotlin")
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar", "*.aar")))
    implementation(kotlin("stdlib-jdk7", rootProject.extra["kotlinVersion"] as String))

    compileOnly("com.google.auto.service:auto-service:+")
    kapt("com.google.auto.service:auto-service:+")
}

extra["publishDependsOn"] = "assembleRelease"
extra["publishArtifact"] = "${project.buildDir}/libs/anno.aar"
extra["publishGroupId"] = rootProject.extra["groupId"]
extra["publishArtifactId"] = "anno"
extra["publishVersion"] = rootProject.extra["versionName"]
apply(from = rootProject.projectDir.absolutePath + "/gradle/publish.gradle.kts")
