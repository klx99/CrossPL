plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

if (rootProject.extra.has("groupId")) {
    extra["publishDependsOn"] = "assemble"
    extra["publishArtifact"] = "${project.buildDir}/libs/buildSrc.jar"
    extra["publishGroupId"] = rootProject.extra["groupId"]
    extra["publishArtifactId"] = "crosspl-gradleplugin"
    extra["publishVersion"] = rootProject.extra["versionName"]
    apply(from = rootProject.projectDir.absolutePath + "/gradle/publish.gradle.kts")
}
