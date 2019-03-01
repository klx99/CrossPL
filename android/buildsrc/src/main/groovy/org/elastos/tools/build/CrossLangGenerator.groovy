package org.elastos.tools.build

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Registers the plugin's tasks.
 *
 * @author mengxk
 */

class CrossLangGenerator implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.logger.warn("generator cross language...")
    }
}
