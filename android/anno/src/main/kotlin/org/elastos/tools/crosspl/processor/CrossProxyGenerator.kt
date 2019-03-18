package org.elastos.tools.crosspl.processor

import java.io.File
import java.util.Scanner

class CrossProxyGenerator {
    companion object {
        fun Generate(crossProxyDir: File, classInfo: CrossClassInfo): Boolean {
            val proxyHeaderFile = File(crossProxyDir, classInfo.classSimpleName + "Proxy.hpp")
            val proxySourceFile = File(crossProxyDir, classInfo.classSimpleName + "Proxy.cpp")

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
            var staticFuncList = ""
            var memberFuncList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, null, it.isNative)
                if(it.isNative) {
                    nativeFuncList += "${TabSpace}static $functionDeclare;\n"
                } else if(it.isStatic) {
                    staticFuncList += "${TabSpace}static $functionDeclare;\n"
                } else {
                    memberFuncList += "${TabSpace}$functionDeclare;\n"
                }
            }
            content = content
                .replace(TmplKeyNativeFunction, nativeFuncList)
                .replace(TmplKeyStaticFunction, staticFuncList)
                .replace(TmplKeyMemberFunction, memberFuncList)

            WriteContent(proxyFile, content)
            return true
        }

        private fun GenerateSource(proxyFile: File, classInfo: CrossClassInfo): Boolean {
            Log.w("Generate: ${proxyFile.absolutePath}")
            val tmpl = ReadTmplContent(CrossClassProxySourceTmpl)
            var content = ConvertCommonInfo(classInfo, tmpl)

            var nativeFuncList = ""
            var staticFuncList = ""
            var memberFuncList = ""
            classInfo.methodInfo.forEach {
                val functionDeclare = GenerateFunctionDeclare(it, classInfo.classSimpleName, it.isNative)
                if(it.isNative) {
                    nativeFuncList += "$functionDeclare\n{\n}\n"
                } else if(it.isStatic) {
                    staticFuncList += "$functionDeclare\n{\n}\n"
                } else {
                    memberFuncList += "$functionDeclare\n{\n}\n"
                }
            }
            content = content
                .replace(TmplKeyNativeFunction, nativeFuncList)
                .replace(TmplKeyStaticFunction, staticFuncList)
                .replace(TmplKeyMemberFunction, memberFuncList)

            WriteContent(proxyFile, content)
            return true
        }

        private fun ConvertCommonInfo(classInfo: CrossClassInfo, tmpl: String): String {
            val content = tmpl
                .replace(TmplKeyNamespace, classInfo.namespace)
                .replace(TmplKeyClassName, classInfo.classSimpleName)

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
                                            className: String?): String {
            var className = (if(className != null) "$className::" else "")
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
                                            className: String?): String {
            var className = (if(className != null) "$className::" else "")
            val returnType = methodInfo.returnType.toCppString(false)
            var content = "$returnType $className${methodInfo.methodName}($TmplKeyArguments)"

            var arguments = ""
            for(idx in methodInfo.paramsType.indices) {
                val type = methodInfo.paramsType[idx].toCppString(true)
                arguments += "${type} var$idx, "
            }
            arguments = arguments.removeSuffix(", ")
            content = content.replace(TmplKeyArguments, arguments)

            return content
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
        private const val TmplKeyStaticFunction = "%StaticFunction%"
        private const val TmplKeyMemberFunction = "%MemberFunction%"
        private const val TmplKeyNativeFunction = "%NativeFunction%"
        private const val TmplKeyArguments = "%Arguments%"
        private const val TabSpace = "  "
    }
}
