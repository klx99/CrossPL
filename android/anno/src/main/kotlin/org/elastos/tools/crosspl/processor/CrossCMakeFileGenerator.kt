package org.elastos.tools.crosspl.processor

import java.io.File
import java.util.Scanner

class CrossCMakeFileGenerator {
    companion object {
        fun Generate(crossProxyDir: File, classInfo: CrossClassInfo): Boolean {
            val proxyHeaderFile = File(crossProxyDir, classInfo.cppClassName + "Proxy.hpp")
            val proxySourceFile = File(crossProxyDir, classInfo.cppClassName + "Proxy.cpp")

            var ret = GenerateHeader(proxyHeaderFile, classInfo)
            if(! ret) {
                return ret;
            }

            ret = GenerateSource(proxySourceFile, classInfo)
            if(! ret) {
                return ret;
            }

            return true
        }

        private fun GenerateHeader(proxyFile: File, classInfo: CrossClassInfo): Boolean {
            Log.w("Generate: ${proxyFile.absolutePath}")
            val tmpl = ReadTmplContent(CrossClassProxyHeaderTmpl)
            var content = ConvertCommonInfo(classInfo, tmpl)

            var nativeFuncList = ""
            var platformFuncList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, null, it.isNative)
                if(it.isNative) {
                    nativeFuncList += "${TabSpace}static $functionDeclare;\n"
                } else {
                    platformFuncList += "${TabSpace}static $functionDeclare;\n"
                }
            }
            content = content
                .replace(TmplKeyPlatformFunction, platformFuncList)
                .replace(TmplKeyNativeFunction, nativeFuncList)

            WriteContent(proxyFile, content)
            return true
        }

        private fun GenerateSource(proxyFile: File, classInfo: CrossClassInfo): Boolean {
            Log.w("Generate: ${proxyFile.absolutePath}")
            val tmpl = ReadTmplContent(CrossClassProxySourceTmpl)
            var content = ConvertCommonInfo(classInfo, tmpl)

            var nativeFuncList = ""
            var platformFuncList = ""
            var jniNativeMethodList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, classInfo.cppClassName, it.isNative)
                if(it.isNative) {
                    nativeFuncList += "$functionDeclare\n{\n}\n"

                    val methodContent = GenerateJniNativeMethod(it)
                    jniNativeMethodList += "$TabSpace$TabSpace$methodContent,\n"
                } else {
                    platformFuncList += "$functionDeclare\n{\n}\n"
                }
            }
            content = content
                .replace(TmplKeyPlatformFunction, platformFuncList)
                .replace(TmplKeyNativeFunction, nativeFuncList)
                .replace(TmplKeyJniNativeMethods, jniNativeMethodList)
                .replace(TmplKeyJniJavaClass, classInfo.javaClassName)

            WriteContent(proxyFile, content)
            return true
        }

        private fun ConvertCommonInfo(classInfo: CrossClassInfo, tmpl: String): String {
            val content = tmpl
                .replace(TmplKeyNamespace, classInfo.cppNamespace)
                .replace(TmplKeyClassName, classInfo.cppClassName)

            return content
        }

        private fun GenerateFunctionDeclare(methodInfo: CrossMethodInfo,
                                            className: String?,
                                            isJniFunc: Boolean): String {
            return if(isJniFunc) {
                GenerateJniFunctionDeclare(methodInfo, className)
            } else {
                GenerateCppFunctionDeclare(methodInfo, className)
            }
        }

        private fun GenerateJniFunctionDeclare(methodInfo: CrossMethodInfo,
                                            cppClassName: String?): String {
            var className = (if(cppClassName != null) "$cppClassName::" else "")
            val returnType = methodInfo.returnType.toJniString(false)
            var content = "$returnType $className${methodInfo.methodName}($TmplKeyArguments)"

            var arguments = "JNIEnv* jenv, jclass jtype, "
            for(idx in methodInfo.paramsType.indices) {
                val type = methodInfo.paramsType[idx].toJniString(true)
                arguments += "${type} jvar$idx, "
            }
            arguments = arguments.removeSuffix(", ")
            content = content.replace(TmplKeyArguments, arguments)

            return content
        }

        private fun GenerateCppFunctionDeclare(methodInfo: CrossMethodInfo,
                                               cppClassName: String?): String {
            var className = (if(cppClassName != null) "$cppClassName::" else "")
            val returnType = methodInfo.returnType.toCppString(false)
            var content = "$returnType $className${methodInfo.methodName}($TmplKeyArguments)"

            var arguments = "uint64_t platformHandle"
            for(idx in methodInfo.paramsType.indices) {
                val type = methodInfo.paramsType[idx].toCppString(true)
                arguments += "${type} var$idx, "
            }
            arguments = arguments.removeSuffix(", ")
            content = content.replace(TmplKeyArguments, arguments)

            return content
        }

        private fun GenerateJniNativeMethod(methodInfo: CrossMethodInfo ): String {
            var funcType = "("
            methodInfo.paramsType.forEach {
                funcType += it.toJavaChar()
            }
            funcType += ")"
            funcType += methodInfo.returnType.toJavaChar()

            val methodContent = "{\"${methodInfo.methodName}\", \"$funcType\", (void*)${methodInfo.methodName}}"

            return methodContent
        }

        private fun ReadTmplContent(tmplName: String): String {
            val istream = CrossClassAnnoProcessor::class.java.getResourceAsStream(tmplName)
            val scanner = Scanner(istream)
            val template = scanner.useDelimiter("\\A").next()

            return template
        }

        private fun WriteContent(file: File, content: String) {
            file.apply {
                parentFile.mkdirs()
                writeText(content)
            }
        }

        private const val CrossClassProxyHeaderTmpl = "/CrossPLClassProxy.hpp.tmpl"
        private const val CrossClassProxySourceTmpl = "/CrossPLClassProxy.cpp.tmpl"

        private const val TmplKeyNamespace = "%Namespace%"
        private const val TmplKeyClassName = "%ClassName%"
        private const val TmplKeyPlatformFunction = "%PlatformFunction%"
        private const val TmplKeyNativeFunction = "%NativeFunction%"
        private const val TmplKeyArguments = "%Arguments%"

        private const val TmplKeyJniJavaClass = "%JniJavaClass%"
        private const val TmplKeyJniNativeMethods = "%JniNativeMethods%"

        private const val TabSpace = "  "
    }
}
