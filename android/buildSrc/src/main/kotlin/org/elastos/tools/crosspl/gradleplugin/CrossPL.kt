package org.elastos.tools.crosspl.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import org.gradle.kotlin.dsl.*


class CrossPL : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        project.logger.warn("generator cross language...")
    }
}
