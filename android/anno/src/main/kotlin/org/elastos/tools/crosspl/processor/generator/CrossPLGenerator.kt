package org.elastos.tools.crosspl.processor.generator

import org.elastos.tools.crosspl.processor.CrossClassInfo
import org.elastos.tools.crosspl.processor.CrossTmplUtils
import org.elastos.tools.crosspl.processor.Log
import java.io.File

class CrossPLGenerator {
    companion object {
        fun Generate(crossplDir: File, classInfoList: List<CrossClassInfo>, headerFileList: List<File>): Boolean {
            val headerFile = GetHeaderFile(crossplDir)
            val sourceFile = GetSourceFile(crossplDir)

            var ret = GenerateHeader(headerFile)
            if(! ret) {
                return ret
            }

            ret = GenerateSource(sourceFile, classInfoList, headerFileList)
            if(! ret) {
                return ret
            }

            return true
        }

        fun GetSourceFile(crossplDir: File): File {
            return File(crossplDir, "CrossPL.cpp")
        }

        fun GetHeaderFile(crossplDir: File): File {
            return File(crossplDir, "CrossPL.hpp")
        }

        private fun GenerateHeader(headerFile: File): Boolean {
            Log.w("Generate: ${headerFile.absolutePath}")
            val content = CrossTmplUtils.ReadTmplContent(CrossPLHeaderTmpl)

            CrossTmplUtils.WriteContent(headerFile, content)
            return true
        }

        private fun GenerateSource(sourceFile: File, classInfoList: List<CrossClassInfo>, headerFileList: List<File>): Boolean {
            Log.w("Generate: ${sourceFile.absolutePath}")
            var content = CrossTmplUtils.ReadTmplContent(CrossPLSourceTmpl)

            var regNativeMethodsList = ""
            classInfoList.forEach {
                regNativeMethodsList += "${CrossTmplUtils.TabSpace}ret |= ${it.cppClassName}Proxy::RegisterNativeMethods(jenv);\n"
            }

            var includeProxyList = ""
            headerFileList.forEach {
                includeProxyList += "#include <${it.name}>\n"
            }

            content = content
                .replace(TmplKeyIncludeProxyHpp, includeProxyList)
                .replace(TmplKeyRegisterNativeMethods, regNativeMethodsList)
                .replace(TmplKeyJniOnLoad, "JNI_OnLoad")

            CrossTmplUtils.WriteContent(sourceFile, content)
            return true
        }

        private const val CrossPLHeaderTmpl = "/CrossPL.hpp.tmpl"
        private const val CrossPLSourceTmpl = "/CrossPL.cpp.tmpl"

        private const val TmplKeyIncludeProxyHpp = "%IncludeProxyHpp%"
        private const val TmplKeyRegisterNativeMethods = "%RegisterNativeMethods%"
        private const val TmplKeyJniOnLoad = "%JniOnLoad%"
    }
}
