package org.elastos.tools.crosslang

import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.kotlin.dsl.*


class CrossLangGenerator : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        project.logger.warn("generator cross language...")
    }
}
