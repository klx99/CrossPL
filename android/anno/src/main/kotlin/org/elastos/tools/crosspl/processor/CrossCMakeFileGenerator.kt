package org.elastos.tools.crosspl.processor

import java.io.File
import java.util.Scanner

class CrossCMakeFileGenerator {
    companion object {
        fun Generate(crossPLDir: File, headerFileList: List<File>, sourceFileList: List<File>): Boolean {
            val cmakefileFile = File(crossPLDir, "CMakeLists.txt")

            Log.w("Generate: ${cmakefileFile.absolutePath}")
            var content = CrossTmplUtils.ReadTmplContent(CrossCMakeListsTmpl)

            var headerContent = ""
            headerFileList.forEach {
                headerContent += "${CrossTmplUtils.TabSpace}'${it.toRelativeString(crossPLDir)}'\n"
            }
            var sourceContent = ""
            sourceFileList.forEach {
                sourceContent += "${CrossTmplUtils.TabSpace}'${it.toRelativeString(crossPLDir)}'\n"
            }
            content = content
                .replace(TmplKeyProxyHeaders, headerContent)
                .replace(TmplKeyProxySources, sourceContent)

            CrossTmplUtils.WriteContent(cmakefileFile, content)
            return true
        }

        private const val CrossCMakeListsTmpl = "/CrossPLCMakeLists.txt.tmpl"

        private const val TmplKeyProxyHeaders = "%CrossProxyHeaders%"
        private const val TmplKeyProxySources = "%CrossProxySources%"
    }
}
