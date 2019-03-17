package org.elastos.tools.crosspl.gradleplugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.File

class CrossPLPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        project.logger.warn("generator cross language...")

        val config = parseConfig(project)
        project.logger.warn("filter CrossBase class with config: ${config}")

        val sourceFiles = filterSourceFiles(project, config)
        val nativeClassInfoList = mutableListOf<NativeClassInfo>()
        sourceFiles.forEach() {
            val nativeClassInfo = parseSourceFile(project, config, it) ?: return@forEach
            nativeClassInfoList.add(nativeClassInfo)
        }
    }

    private fun parseConfig(project: Project): Config {
        val config = Config()

        if(!project.extra.has("crosspl")) {
            return config
        }

        @Suppress("UNCHECKED_CAST")
        val extra = project.extra["crosspl"] as Map<String, Any>
        if(extra.containsKey("sourceList")) {
            @Suppress("UNCHECKED_CAST")
            config.sourceList = extra["sourceList"] as List<String>
        }
        if(extra.containsKey("withOnLoadFunc")) {
            config.withOnLoadFunc = extra["withOnLoadFunc"] as Boolean
        }

        return config
    }

    private fun filterSourceFiles(project: Project, config: Config): List<File> {
        val androidExtension = project.extensions.getByName("android") as BaseExtension
        val mainSourceSets = androidExtension.sourceSets.findByName("main")
        val javaSrcDirs = mainSourceSets?.java?.srcDirs

        val crossBaseFiles = mutableListOf<File>()
        javaSrcDirs?.forEach {
            project.logger.warn("filter CrossBase class from: ${it.absolutePath}")

            it.walk().forEach javaSrc@ {
                if(it.isDirectory) {
                    return@javaSrc
                }

                if(!config.sourceList.isEmpty()
                && !config.sourceList.contains(it.name)) {
                    return@javaSrc
                }

                project.logger.warn("add CrossBase class file: ${it.absolutePath}")
                crossBaseFiles.add(it)
            }
        }

        return crossBaseFiles
    }

    private fun parseSourceFile(project: Project, config: Config, file: File): NativeClassInfo? {
        var info: NativeClassInfo? = null

        when(file.extension) {
            "java" -> info = parseJavaFile(project, config, file)
            "kt" -> info = parseKotlinFile(project, config, file)
        }

        return info
    }

    private fun parseJavaFile(project: Project, config: Config, file: File): NativeClassInfo? {
        var info: NativeClassInfo? = null

//        throw Exception("UNIMPLETEMENT")

        return info
    }

    private fun parseKotlinFile(project: Project, config: Config, file: File): NativeClassInfo? {
        var info: NativeClassInfo? = null


        return info
    }
}
