package org.elastos.tools.crosspl.processor.generator

import org.elastos.tools.crosspl.processor.CrossClassInfo
import org.elastos.tools.crosspl.processor.CrossTmplUtils
import org.elastos.tools.crosspl.processor.Log
import java.io.File

class CrossPLFactoryGenerator {
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
            return File(crossplDir, "CrossPLFactory.cpp")
        }

        fun GetHeaderFile(crossplDir: File): File {
            return File(crossplDir, "CrossPLFactory.hpp")
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

            var includeProxyList = ""
            headerFileList.forEach {
                includeProxyList += "#include <${it.name}>\n"
            }

            var regNativeMethodsList = ""
            var createCppObjectList = ""
            var destroyCppObjectList = ""
            classInfoList.forEach {
                includeProxyList += "#include <${it.cppClassName}.hpp>\n"

                regNativeMethodsList += "${CrossTmplUtils.TabSpace}ret |= ${it.cppClassName}Proxy::RegisterNativeMethods(jenv);\n"

                createCppObjectList += "${CrossTmplUtils.TabSpace}if(std::strcmp(javaClassName, \"${it.javaClassName}\") == 0) {\n"
                createCppObjectList += "${CrossTmplUtils.TabSpace}${CrossTmplUtils.TabSpace} ptr = new ${it.cppClassName}();\n"
                createCppObjectList += "${CrossTmplUtils.TabSpace}}\n"

                destroyCppObjectList += "${CrossTmplUtils.TabSpace}if(std::strcmp(javaClassName, \"${it.javaClassName}\") == 0) {\n"
                destroyCppObjectList += "${CrossTmplUtils.TabSpace}${CrossTmplUtils.TabSpace}delete reinterpret_cast<${it.cppClassName}*>(cppHandle);\n"
                destroyCppObjectList += "${CrossTmplUtils.TabSpace}${CrossTmplUtils.TabSpace}return 0;\n"
                destroyCppObjectList += "${CrossTmplUtils.TabSpace}}\n"
            }

            content = content
                .replace(TmplKeyIncludeProxyHpp, includeProxyList)
                .replace(TmplKeyRegisterNativeMethods, regNativeMethodsList)
                .replace(TmplKeyCreateCppObject, createCppObjectList)
                .replace(TmplKeyDestroyCppObject, destroyCppObjectList)
                .replace(TmplKeyJniOnLoad, "JNI_OnLoad")

            CrossTmplUtils.WriteContent(sourceFile, content)
            return true
        }

        private const val CrossPLHeaderTmpl = "/CrossPLFactory.hpp.tmpl"
        private const val CrossPLSourceTmpl = "/CrossPLFactory.cpp.tmpl"

        private const val TmplKeyIncludeProxyHpp = "%IncludeProxyHpp%"
        private const val TmplKeyRegisterNativeMethods = "%RegisterNativeMethods%"
        private const val TmplKeyCreateCppObject = "%CreateCppObject%"
        private const val TmplKeyDestroyCppObject = "%DestroyCppObject%"
        private const val TmplKeyJniOnLoad = "%JniOnLoad%"
    }
}
