package org.elastos.tools.crosspl.processor.generator

import org.elastos.tools.crosspl.processor.CrossTmplUtils
import org.elastos.tools.crosspl.processor.Log
import java.io.File

class CrossPLUtilsGenerator {
    companion object {
        fun Generate(crossplDir: File): Boolean {
            val headerFile = GetHeaderFile(crossplDir)
            val sourceFile = GetSourceFile(crossplDir)

            var ret = GenerateHeader(headerFile)
            if(! ret) {
                return ret
            }

            ret = GenerateSource(sourceFile)
            if(! ret) {
                return ret
            }

            return true
        }

        fun GetSourceFile(crossplDir: File): File {
            return File(crossplDir, "CrossPLUtils.cpp")
        }

        fun GetHeaderFile(crossplDir: File): File {
            return File(crossplDir, "CrossPLUtils.hpp")
        }

        private fun GenerateHeader(headerFile: File): Boolean {
            Log.w("Generate: ${headerFile.absolutePath}")
            val content = CrossTmplUtils.ReadTmplContent(CrossPLUtilsHeaderTmpl)

            CrossTmplUtils.WriteContent(headerFile, content)
            return true
        }

        private fun GenerateSource(sourceFile: File): Boolean {
            Log.w("Generate: ${sourceFile.absolutePath}")
            val content = CrossTmplUtils.ReadTmplContent(CrossPLUtilsSourceTmpl)

            CrossTmplUtils.WriteContent(sourceFile, content)
            return true
        }

        private const val CrossPLUtilsHeaderTmpl = "/CrossPLUtils.hpp"
        private const val CrossPLUtilsSourceTmpl = "/CrossPLUtils.cpp"
    }
}
