import org.gradle.api.internal.artifacts.dependencies.DefaultProjectDependency
import org.gradle.api.publish.PublishingExtension

apply(plugin =  "maven-publish")

configure<PublishingExtension> {
    repositories {
        maven {
            url = uri(project.buildDir.absolutePath + "/repo")
        }
    }
    publications {
        register("maven", MavenPublication::class) {
            groupId = project.extra["publishGroupId"] as String
            artifactId = project.extra["publishArtifactId"] as String
            version = project.extra["publishVersion"] as String

            artifact(project.extra["publishArtifact"])

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")

                val implementation = project.configurations.getByName("implementation")
                implementation.allDependencies.forEach {
                    val depClass = it::class.toString()
                    if(!depClass.contains("DefaultExternalModuleDependency")
                        && !depClass.contains("DefaultProjectDependency")) {
                        println("ignore dependency " + depClass)
                        return@forEach
                    }
                    if(it.name.contains("kotlin-stdlib")) {
                        println("ignore dependency " + it.name)
                        return@forEach
                    }
                    println("add depend project " + it.name)

                    var depGroup = it.group
                    var depName = it.name
                    var depVersion = it.version
                    if(depClass.contains("DefaultProjectDependency")) {
                        var depProject = (it as DefaultProjectDependency).dependencyProject
                        depName = depProject.name
                        depGroup = depProject.extra["groupId"] as String
                        depVersion = depProject.extra["versionName"] as String
                    }

                    println("add dependency " + it.name)
                    var dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", depGroup)
                    dependencyNode.appendNode("artifactId", depName)
                    dependencyNode.appendNode("version", depVersion)
                    dependencyNode.appendNode("scope", "compile")
                }
            }
        }
        return@publications
    }
}

tasks.withType<PublishToMavenRepository> {
  this.dependsOn(project.extra["publishDependsOn"])
}
